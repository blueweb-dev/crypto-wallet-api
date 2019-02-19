package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class SystemStatusResponse extends WalletResponse {
  
    private SystemStatus result;
    public void setResult(float cpu, float mem, float disk, long localBlockHeight
    		, long infuraBlockHeight) {
    	this.result = new SystemStatus(cpu, mem, disk, localBlockHeight
    			, infuraBlockHeight);
    }
    
    @Data public class SystemStatus {
    	private float cpu, mem, disk;
    	private long localBlockHeight, infuraBlockHeight;
    	
    	public SystemStatus(float cpu, float mem, float disk, long localBlockHeight
    			, long infuraBlockHeight) {
    		this.cpu = cpu;
    		this.mem = mem;
    		this.disk = disk;
    		this.localBlockHeight = localBlockHeight;
    		this.infuraBlockHeight = infuraBlockHeight;
    	}
    	
    }
}
