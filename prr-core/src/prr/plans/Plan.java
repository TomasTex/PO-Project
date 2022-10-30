package prr.plans;

import java.io.Serializable;

import prr.comms.Communication;

public abstract class Plan implements Serializable {

    /** Serial number for serialization. */
	private static final long serialVersionUID = 202208091753L;

    public abstract long calculateTextCommPrice(Communication comm);
    public abstract long calculateVoiceCommPrice(Communication comm);
    public abstract long calculateVideoCommPrice(Communication comm);
    
}
