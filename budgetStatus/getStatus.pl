#!/opt/homebrew/bin/perl

use warnings;
use strict;

my $dailyAmountFilename = "../DollarsPerDayExpenditures.csv";

open(my $fh,"<",$dailyAmountFilename) or die "Can't open $dailyAmountFilename";

my $line = <$fh>;

chomp ($line);

my ($date, $title) = split(',',$line);

printf("Date: %s - Title: %s - Converted to MONTHLY (dollars per month)\n", $date, $title);

chomp(my @lines = <$fh>);

my @sorted_lines = sort @lines;
#{ lines->[1] cmp lines->[1] } 

foreach $line (@sorted_lines) {
    my ($category, $amount) = split (',',$line);
    printf("\"%-.2f\",\"%s\"\n", 31*$amount, uc $category);
}

