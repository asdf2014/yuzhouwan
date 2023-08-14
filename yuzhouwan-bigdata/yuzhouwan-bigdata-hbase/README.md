# Add Slat for solving 'Hot Region' Problem in HBase

## Windows

### Download

#### Google Protocol Buffers

```bash
wget https://github.com/google/protobuf/releases/download/v3.1.0/protoc-3.1.0-win32.zip -c -O protoc-3.1.0-win32.zip
```

### Code

#### DataProtos.proto

linked to resources/salt/DataProtos.proto

### Generate

#### Proto

```bash
protoc.exe DataProtos.proto --java_out=.
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
