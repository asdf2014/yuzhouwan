# !/usr/bin/env python
# -*- coding:utf-8 -*-

table_permission = {}

# echo "scan 'hbase:acl'" | hbase shell > acl.txt
for line in open("acl.txt"):
    info = line.strip().rstrip().split(",")
    if len(info) == 3:
        table = info[0]
        table_info = table.split(" ")
        acl = info[2].strip(" value=")
        if len(table_info) == 2:
            table_name = table_info[0]
            user_name = table_info[1].split("column=l:")
            if len(user_name) == 2:
                user_name = user_name[1]
                if user_name != 'hbase':  # remove hbase user
                    if table_name in table_permission:
                        table_permission[table_name].append([user_name, acl])
                    else:
                        table_permission[table_name] = [[user_name, acl]]

table_permission_right = {}
table_permission_wrong = {}
table_permission_wrong_namespace = {}

for table_name in table_permission:
    namespace_table = table_name.split(":")
    if len(namespace_table) == 2:
        namespace = namespace_table[0].split("ns_")
        if len(namespace) == 2:
            namespace = namespace[1]
            user_name_acl = table_permission[table_name]
            if len(user_name_acl) == 1:
                if user_name_acl[0][0] == namespace:
                    table_permission_right[table_name] = user_name_acl
                else:
                    # table_permission_wrong[table_name] = user_name_acl
                    table_permission_wrong_namespace[table_name] = user_name_acl
            else:
                is_wrong = False
                for acl in user_name_acl:
                    if acl[0] != namespace and ('W' in acl[1] or 'A' in acl[1]):  # across user write
                        is_wrong = True
                        break
                if is_wrong:
                    table_permission_wrong[table_name] = user_name_acl
                else:
                    table_permission_right[table_name] = user_name_acl

print "# Permission Right Tables"
for table_name in table_permission_right:
    print table_name, table_permission_right[table_name]

print "\n\n\n"

print "# Permission Wrong Tables"

print "\n"

print "## Wrong UserName"

for table_name in table_permission_wrong_namespace:
    print table_name, table_permission_wrong_namespace[table_name]

print "\n\n\n"

print "## Cross User Write"

for table_name in table_permission_wrong:
    print table_name, table_permission_wrong[table_name]

print "\n\n\n"

table_permission_abnormal_namespace_level = {}
table_permission_abnormal_without_namespace = {}
table_permission_abnormal_others = {}

print "# Abnormal Tables"
for table_name in table_permission:
    if table_name not in table_permission_right and table_name not in table_permission_wrong \
            and table_name not in table_permission_wrong_namespace:
        if table_name.startswith("@"):  # namespace level permission
            table_permission_abnormal_namespace_level[table_name] = table_permission[table_name]
        elif ':' not in table_name:
            table_permission_abnormal_without_namespace[table_name] = table_permission[table_name]
        else:
            table_permission_abnormal_others[table_name] = table_permission[table_name]

print "\n"

print "## Namespace Level"
for table_name in table_permission_abnormal_namespace_level:
    print table_name, table_permission_abnormal_namespace_level[table_name]

print "\n\n\n"

print "## Without Namespace"
for table_name in table_permission_abnormal_without_namespace:
    print table_name, table_permission_abnormal_without_namespace[table_name]

print "\n\n\n"

print "# Others"
for table_name in table_permission_abnormal_others:
    print table_name, table_permission_abnormal_others[table_name]

print "\n# Result"

right_num = len(table_permission_right)
wrong_username_num = len(table_permission_wrong)
wrong_cross_user_num = len(table_permission_wrong_namespace)
wrong_num = wrong_username_num + wrong_cross_user_num
all_num = len(table_permission)
abnormal_num = all_num - right_num - wrong_num
abnormal_namespace_level = len(table_permission_abnormal_namespace_level)
abnormal_without_namespace = len(table_permission_abnormal_without_namespace)
abnormal_others = len(table_permission_abnormal_others)
print "Right : Wrong(username/cross-user) : Abnormal(namespace/without_namespace/other) : " \
      "All = %d : %d (%d/%d) : %d (%d/%d/%d) : %d" % \
      (right_num, wrong_num, wrong_username_num, wrong_cross_user_num, abnormal_num,
       abnormal_namespace_level, abnormal_without_namespace, abnormal_others, all_num)
