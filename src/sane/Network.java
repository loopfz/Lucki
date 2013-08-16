package sane;
/* Network.java */

/* network definition from sane.h */

public class Network {

Network() {
  input = new float [Config.NUM_INPUTS];
  sigout = new float [Config.NUM_OUTPUTS];
  sum = new float [Config.NUM_OUTPUTS];
  hidden = new Neuron [Config.NUM_HIDDEN];
}

public float input[];
public float sigout[];
public float sum[];
public Neuron hidden[];

public int winner;	/* highest output unit */

}
