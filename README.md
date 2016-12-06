# Java-Instrumenter

USAGE:

make build/rebuild

make preprocess ARGS="source_file"（保证每行只有一条语句。一般不需要预处理。）

make instrument ARGS="-d source_file_dir -t trace_file_path"

make parse ARGS="trace_file_path"

TODO:

变量作用域和for循环（这是个大问题

用反射查看类的成员变量

空循环控制流追踪有bug
