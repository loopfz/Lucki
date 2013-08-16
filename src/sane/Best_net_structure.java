package sane;

/* Config.java */

/* This structure keeps "pointers" to the best networks of the
   previous generation.  This is the genetic description for the
   network level evolution.  Previously in sane.h. */

public class Best_net_structure {

  Best_net_structure() {
    neuron = new Neuron [Config.NUM_HIDDEN];
  }

public Neuron neuron[];

public float fitness;

}
