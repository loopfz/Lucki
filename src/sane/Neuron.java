package sane;
/* Neuron.java */

/* neuron genetic structure from sane.h */

public class Neuron {

public Neuron() {
  gene = new float [Config.GENE_SIZE];
  in_conn = new int [Config.GENE_SIZE/2];
  out_conn = new int [Config.GENE_SIZE/2];
  in_weight = new float [Config.GENE_SIZE/2];
  out_weight = new float [Config.GENE_SIZE/2];
  in_delta = new float [Config.GENE_SIZE/2];
  out_delta = new float [Config.GENE_SIZE/2];
}

public float gene[];
public int in_conn[]; 
public int out_conn[]; 
public float in_weight[];
public float out_weight[];
public float in_delta[];
public float out_delta[];

public int numin;
public int numout;
public boolean decoded;	/* has neuron already been decode? */
public float fitness;	/* neuron's fitness value */
public int tests;	/* holds the # of networks participated in */
public int ranking; 	/* neuron's rank in population */
public int type;
public double sum;
public char output;
public float sigout;
public float error;

}
