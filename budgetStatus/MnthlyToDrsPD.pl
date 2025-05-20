#!/opt/homebrew/bin/perl

use warnings;
use strict;

my $mnthlyAmountFilename = "../DollarsPerMonth.csv";

open(my $fh,"<",$mnthlyAmountFilename) or die "Can't open $mnthlyAmountFilename";

my $line = <$fh>;

chomp ($line);

my $title;

(my $date, $title) = split(',',$line);

printf("%s, %s\n", $date, $title);

chomp(my @lines = <$fh>);

my @sorted_lines = sort @lines;
#{ lines->[1] cmp lines->[1] } 

printf STDERR ("First line is: %s\n", $sorted_lines[0]);
printf STDERR ("\e[31mIf there is an error... try deleting all blank lines because when they're sorted they\e[0m\n");
printf STDERR ("\e[31mtrickle up to the top and there is nothing in the line after they're 'chomped'\e[0m\n");

foreach $line (@sorted_lines) {
    my ( $category, $amount ) = split (',',$line);
    $amount =~ s/\"//g;
    printf("%s,%-.2f\n", uc $category, $amount/30.4167);
}

