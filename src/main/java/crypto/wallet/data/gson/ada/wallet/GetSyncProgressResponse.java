package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data
public class GetSyncProgressResponse {
	SyncProgress data;
	String status;
	Meta meta;
}
