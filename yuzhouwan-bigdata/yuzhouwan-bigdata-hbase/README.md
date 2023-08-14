# Solving 'Hot Region' problem in HBase by adding Slat

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
