var a = "123abc456def789g"
var b = """(\d\d\d)""".r
b findAllIn a toList

a = "123abc456def789g"
b = """(\d\d\d)\w{3}(\d\d\d)\w{3}(\d\d\d)\w{1}""".r
var c = b findAllIn a
c.groupNames
c.groupCount
c.group(1)

val d = """(\d\d\d)\w{3}(\d\d\d)\w{3}(\d\d\d)\w{1}""".r
a match {
  case d(d1, d2, d3) => print(d1, d2, d3)
}

for(data <- c.matchData; r <- data.subgroups) println(r)

val str = "2017-01-12 07:00:00,648 INFO org.apache.hadoop.hdfs.server.namenode.FSNamesystem.audit: allowed=true ugi=aps (auth:SIMPLE) ip=/10.27.236.67 cmd=listStatus src=/user/aps/admds/priceTextRealtime/aa/bb dst=null perm=null"
val regex = """(?<=ugi=)(?<ugi>\w+).*(?<= src=)(?<src>.*(?= dst))""".r
for(data <- regex.findAllIn(str).matchData; r <- data.subgroups) println(r)

regex.findAllIn(str).matchData.foreach(
  r => r.subgroups.foreach(
    sub => println(sub)
  )
)
