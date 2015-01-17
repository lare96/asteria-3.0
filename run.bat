@echo off
title asteria 3.0
"C:/Program Files (x86)/Java/jdk1.8.0_25/bin/java.exe" -Xmx1024m -cp bin;deps/gson-2.2.4.jar;deps/guava-18.0.jar; com.asteria.Server 
pause