#!/opt/homebrew/bin/perl

use warnings;
use strict;

my $dailyAmountFilename = "../DollarsPerDayExpenditures.csv";
my $mnthlyAmountFilename = "../DollarsPerMonth.csv";

open(my $fh,"<",$mnthlyAmountFilename) or die "Can't open $mnthlyAmountFilename";

my $line = <$fh>;

chomp ($line);

my $title;

(my $date, $title) = split(',',$line);

printf("%s, %s - Converted to DAILY (dollars per day)\n", $date, $title);

chomp(my @lines = <$fh>);

my @sorted_lines = sort @lines;
#{ lines->[1] cmp lines->[1] } 

foreach $line (@sorted_lines) {
    my ( $amount, $category ) = split (',',$line);
    $amount =~ s/\"//g;
    printf("%-.2f,%s\n", $amount/31, uc $category);
}

