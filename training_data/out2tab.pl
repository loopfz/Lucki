#!/usr/bin/perl -w
use Data::Dumper;

my $file = $ARGV[0] or die "No input file\n";

my $gen = 0;

my @generations;

my $k = 0;
while ($k < 97) {
  my $l = 0;
  push(@generations, [{fitness=>0, stack=>0}]);
  while ($l < 99) {
    push(@{$generations[$k]}, { fitness => 0, stack => 0 });
    $l++;
  }
  $k++;
}

open FILE, $file or die "open failed\n";

while (<FILE>) {
  if (/^\s*eval\s*(\d+):\s*([\d\.]+)\s*\[(\d+)\]\s*$/) {
    $generations[$gen]->[$1 - 1]->{fitness} = $2;
    $generations[$gen]->[$1 - 1]->{stack} = $3;
  }
  elsif (/^\s*Generation\s*(\d+)\s*done\s*$/) {
    $gen++;
  }
}

my $i = 0;
while ($i < scalar(@{$generations[0]})) {
  my $j = 0;
  while ($j < scalar(@generations)) {
    print $generations[$j]->[$i]->{fitness};
    if ($j < scalar(@generations) - 1) {
      print ",";
    }
    $j++;
  }
  print "\n";
  $i++;
}

close FILE;
