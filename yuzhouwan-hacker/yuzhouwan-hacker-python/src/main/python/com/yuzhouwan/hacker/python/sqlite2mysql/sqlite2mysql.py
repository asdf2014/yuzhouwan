import datetime
import re


class SQLParser:
    def __init__(self, input_file):
        self.buffer_string = ""
        self.fin = open(input_file)
        self.schema_file = input_file + ".schema.sql"
        self.data_file = input_file + ".data.sql"

        self.fw_schema = open(self.schema_file, "w", buffering=0)
        self.fw_data = open(self.data_file, "w")

        self.previous_string_quote = ""
        self.buffer_string = ""
        self.literal_string = ""
        self.current_char = ""
        self.prev_char = ""
        self.next_char = ""

        self.current_quote = ""
        self.current_line = ""
        self.current_create_table_statement_bracket_count = 0

        # better set the encoding in the database first
        self.fw_data.write("SET NAMES 'utf8' COLLATE 'utf8_general_ci';\n")

        return

    def flush_buffer(self, skip_last_char=False, write_to_file=False):
        if skip_last_char:
            final_buffer = self.buffer_string[:-1]
            self.buffer_string = self.buffer_string[-1]  # clean all except last char
        else:
            final_buffer = self.buffer_string
            self.buffer_string = ""  # clean all

        if self.is_in_quote():
            final_buffer = self.process_literal(final_buffer)  # do misc final processing

        self.current_line += final_buffer

        if write_to_file:
            if self.current_line.startswith("INSERT INTO"):
                self.fw_data.write(self.current_line)
                if not self.current_line.strip().endswith(";"):
                    self.fw_data.write(";\n")
            else:
                self.current_line = self.process_schema(self.current_line)
                self.fw_schema.write(self.current_line)

            self.current_line = ""
        return

    def add_buffer(self, c):
        self.buffer_string += c

    def read_next_char(self):
        self.prev_char = self.current_char
        self.current_char = self.next_char
        self.next_char = self.fin.read(1)

        if self.current_char:
            self.add_buffer(self.current_char)
        elif self.next_char:  # for the first char of the file
            self.read_next_char()

        return self.current_char

    def set_current_quote(self, c):
        self.current_quote = c

    def clean_current_quote(self):
        self.current_quote = ""

    def is_in_quote(self):
        return self.current_quote != ""

    @staticmethod
    def is_skip_line(value):  # no need to copy this line
        return value.startswith("BEGIN TRANSACTION") or value.startswith("COMMIT") or \
               value.startswith("sqlite_sequence") or value.startswith("CREATE UNIQUE INDEX") or \
               value.startswith("PRAGMA")

    def is_in_create_table(self):
        bracket_count = 0
        if self.buffer_string.strip().startswith("CREATE TABLE"):
            bracket_count += self.buffer_string.count("(")
            bracket_count -= self.buffer_string.count(")")

        return bracket_count > 0

    def start(self):
        line_number = 1
        start_time = datetime.datetime.now()

        while True:
            c = self.read_next_char()
            if not c:
                print "End of file"
                break

            if c == "'" or c == "\"":
                if not self.is_in_quote():
                    self.flush_buffer(skip_last_char=True)
                    self.set_current_quote(c)

                elif self.current_quote == c:  # end of string
                    if self.next_char == c:  # double single quote, or double double quote
                        self.read_next_char()  # discard the paired one
                        continue
                    else:
                        self.flush_buffer()
                        self.clean_current_quote()

            if c == "\n" or c == "\r":
                # flush teh buffer
                line_number += 1
                if line_number % 10000 == 0:
                    print "Processing line: ", line_number, "elapsed: ", datetime.datetime.now() - start_time, "seconds"
                if not self.is_in_quote() and not self.is_in_create_table():
                    self.flush_buffer(write_to_file=True)

        # flush the last buffer
        self.flush_buffer(write_to_file=True)
        return

    # HACKING POINT, process literal strings
    def process_literal(self, value):
        # print "@75: processing literal", value

        if value == 't':
            return 1
        if value == 'f':
            return 0
        if self.current_line.endswith("INSERT INTO "):
            return value.strip("\"")  # mysql has no quote for insert into table name
        value = value.replace("\\", "\\\\")
        return value

    # HACKING POINT, process schema
    def process_schema(self, value):

        if self.is_skip_line(value):
            return ""

        new_value = value

        # http://stackoverflow.com/questions/18671/quick-easy-way-to-migrate-sqlite3-to-mysql
        new_lines = []
        for line in new_value.split("\n"):
            searching_for_end = False

            # this line was necessary because ''); was getting
            # converted (inappropriately) to \');
            if re.match(r".*, ''\);", line):
                line = re.sub(r"''\);", r'``);', line)

            if re.match(r'^CREATE TABLE.*', line):
                searching_for_end = True

            m = re.search('CREATE TABLE "?([a-z_]*)"?(.*)', line)
            if m:
                name, sub = m.groups()
                line = "DROP TABLE IF EXISTS `%(name)s` ;\nCREATE TABLE IF NOT EXISTS `%(name)s`%(sub)s\n"
                line = line % dict(name=name, sub=sub)

            # Add auto_increment if it's not there since sqlite auto_increments ALL
            # primary keys
            if searching_for_end:
                if re.search(r"integer(?:\s+\w+)*\s*PRIMARY KEY(?:\s+\w+)*\s*,", line):
                    line = line.replace("PRIMARY KEY", "PRIMARY KEY AUTO_INCREMENT")
                # replace " and ' with ` because mysql doesn't like quotes in CREATE commands
                if line.find('DEFAULT') == -1:
                    line = line.replace('"', '`').replace("'", '`')
                else:
                    parts = line.split('DEFAULT')
                    parts[0].replace('"', '`').replace("'", '`')
                    line = 'DEFAULT'.join(parts)

            # And now we convert it back (see above)
            if re.match(r".*, ``\);", line):
                line = re.sub(r'``\);', r"'');", line)

            if searching_for_end and re.match(r'.*\);', line):
                searching_for_end = False

            if re.match(r"CREATE INDEX", line):
                line = re.sub('"', '`', line)

            new_lines.append(line)

        new_value = "\n".join(new_lines)
        return new_value


def main():
    if __name__ == "__main__":
        # if len(sys.argv) != 2:
        #     print "Usage: python " + sys.argv[0] + " input_file\n"
        #     return -1
        #
        # input_file = sys.argv[1]
        input_file = "E:/Core Code/yuzhouwan/yuzhouwan-hacker/yuzhouwan-hacker-python/src/main/resources/sqlite2mysql/superset.sql"

        parser = SQLParser(input_file)
        parser.start()

        print "Done."
        print "Schema and data files are generated."


main()
