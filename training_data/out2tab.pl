#!/usr/bin/perl -w

my $file = $ARGV[0] or die "No input file\n";

my $gen = 0;

my @generations;

open FILE, $file or die "open failed\n";

while (<>) {
  if (/^\s*eval\s*(\d+):\s*([\d\.]+)\s*\[(\d+)\]\s*$/) {
    $generations[$gen]->[$1 - 1]->{fitness} = $2;
    $generations[$gen]->[$1 - 1]->{stack} = $3;
  }
  elsif (/^\s*Generation\s*(\d+)\s*done\s*$/) {
    $gen++;
  }
}

my $i = 0;
while ($i < scalar(@$generations[0])) {
  my $j = 0;
  while ($j < scalar(@generations)) {
    print $generations[$j]->[$i];
    if ($j < scalar(@generations) - 1) {
      print ',';
    }
    print "\n";
    $j++;
  }
  $i++;
}

close FILE;
