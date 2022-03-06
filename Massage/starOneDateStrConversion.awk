#!/bin/awk -x
BEGIN { FS="[,~]" }
NR==1 {
    print
    next
}

#do all other lines with this
{
    printf("%s,%.4d-%.2d-%.2d 00:00:00,%s,%s,%s,%s,%s\n",$1,$4,$2,$3,$5,$6,$7,$8,$9,$10,$11);
}
