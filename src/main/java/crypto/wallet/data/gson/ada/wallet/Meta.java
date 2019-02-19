package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data
public class Meta {
  
	Pagination pagination;
	
	@Data
	public class Pagination{
		long totalPages;
		long page;
		long perPage;
		long totalEntries;
	}
}
