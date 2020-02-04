#!/bin/bash
# Duration in order to up a new smoc in replica
jvm=$(grep "Started" log | awk -F 'in | seconds' '{print $2}')
ckpt=$(grep "Applied" log | awk -F 'in | seconds' '{print $2}')
sum=$(echo "$jvm" + "$ckpt" | bc)
echo "Sum for duration is ""$sum"

# Learn PID
# xargs will do trimming
current_pid=$(grep "PID" log | awk -F "PID@HOSTNAME is " '{print $2}' | cut -d'@' -f1 | xargs)
echo "PID is ""$current_pid"

# Memory Report from /proc/<pid>/status
# xargs will do trimming
VmSize=$(grep "VmSize" /proc/"$current_pid"/status | awk -F 'VmSize:|kB' '{print $2}' | xargs)
VmPeak=$(grep "VmPeak" /proc/"$current_pid"/status | awk -F 'VmPeak:|kB' '{print $2}' | xargs)
echo "VmPeak is ""$VmPeak"
echo "VmSize is ""$VmSize"

# Memory Report from top command
# -v for awk : define variable
# -n for top : exit after n iteration
from_top=$(top -n 1 | awk -v search="$current_pid" '$1 == search {print $5}' | cut -d'm' -f1)
echo "Current memory consumption is ""$from_top"