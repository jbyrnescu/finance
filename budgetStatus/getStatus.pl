#!/opt/homebrew/bin/perl

use warnings;
use strict;

my $dailyAmountFilename = "../DollarsPerDayExpenditures.csv";

open(my $fh,"<",$dailyAmountFilename) or die "Can't open $dailyAmountFilename";

my $line = <$fh>;

chomp ($line);

my ($date, $title) = split(',',$line);

printf("%s, %s\n", $date, $title);

chomp(my @lines = <$fh>);

my @sorted_lines = sort @lines;
#{ lines->[1] cmp lines->[1] } 

foreach $line (@sorted_lines) {
    my ($category, $amount) = split (',',$line);
    printf("%s,%-.2f\n", uc $category, 30.4167*$amount);
}

