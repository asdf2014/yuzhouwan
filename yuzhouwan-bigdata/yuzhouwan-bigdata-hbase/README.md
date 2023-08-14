# Add Slat for solving 'Hot Region' Problem in HBase

## Download

### Google Protocol Buffers

```bash
wget https://github.com/google/protobuf/releases/download/v3.1.0/protoc-3.1.0-win32.zip -c -O protoc-3.1.0-win32.zip
```

## Code

### DataProtos.proto

linked to resources/salt/DataProtos.proto

## Generate

### Proto

```bash
protoc.exe DataProtos.proto --java_out=.
```
