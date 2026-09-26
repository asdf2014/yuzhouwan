# Solving 'Hot Region' problem in HBase by adding Slat

## HBase 3 protobuf namespace

HBase 3 coprocessor services use
`org.apache.hbase.thirdparty.com.google.protobuf`, including the generated
`DataProtos.java`. After generating with protoc 3.9.1 as below, relocate the
protobuf references before copying the file into `src/main/java`:

```sh
python3 - <<'PY'
from pathlib import Path

generated = Path('com/yuzhouwan/bigdata/hbase/util/salt/DataProtos.java')
generated.write_text(generated.read_text().replace(
    'com.google.protobuf', 'org.apache.hbase.thirdparty.com.google.protobuf'))
PY
```

`DataProtosTest` checks the shaded service descriptor and request/response
serialization against the HBase runtime.

## Windows

```bash
# Download google protocol buffers
$ wget https://github.com/google/protobuf/releases/download/v3.9.1/protoc-3.9.1-win32.zip -c -O protoc-3.9.1-win32.zip

# Generate code to resources/salt/DataProtos.proto
$ protoc.exe DataProtos.proto --java_out=.
```

## MacOS

```bash
$ cd ~/apps
$ mkdir protoc
$ cd protoc
$ wget https://github.com/protocolbuffers/protobuf/releases/download/v3.9.1/protoc-3.9.1-osx-x86_64.zip
$ unzip protoc-3.9.1-osx-x86_64.zip
$ vim ~/.bashrc
```

```bash
export PROTOC_HOME=/Users/yuzhouwan/apps/protoc
export PATH=$PATH:$PROTOC_HOME/bin
```

```bash
$ source ~/.bashrc
$ protoc DataProtos.proto --java_out=.
```
