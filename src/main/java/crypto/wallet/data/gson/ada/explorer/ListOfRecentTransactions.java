package crypto.wallet.data.gson.ada.explorer;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ListOfRecentTransactions {
//	{
//  "Right": [
//      {
//          "cteId": "5c07ad93b1e66a8eb4124b64d96b675cc1cd55e29af68f366c6fa4ec50340f8e",
//          "cteTimeIssued": 1521712951,
//          "cteAmount": {
//              "getCoin": "239654193"
//          }
//      },
//      {
//          "cteId": "a49f3927b1db6638bee763d903e0bcc722502f14d6240df0f7f8a46baa45c114",
//          "cteTimeIssued": 1521712871,
//          "cteAmount": {
//              "getCoin": "169999829018"
//          }
//      },
//      {
//          "cteId": "772d9038371b368a317237039778f33ca89e0d910f020ee47456ad8d281df3be",
//          "cteTimeIssued": 1521712871,
//          "cteAmount": {
//              "getCoin": "8046853725"
//          }
//      },
//      {
//          "cteId": "4044c36650763462a7673e4ff429866364ff7780071398f73c824ea71b41be60",
//          "cteTimeIssued": 1521712871,
//          "cteAmount": {
//              "getCoin": "99999828930"
//          }
//      },
//      {
//          "cteId": "94e3a4f180b0b4edb3c58485ab65e180c5112e5f42e8f6fabe13389a34512d3b",
//          "cteTimeIssued": 1521712791,
//          "cteAmount": {
//              "getCoin": "64382624702"
//          }
//      },
//      {
//          "cteId": "5e50f57b6f549e018ab924e4d2133fb69d4ac58f8f0c961b60b762ffa7281a70",
//          "cteTimeIssued": 1521712751,
//          "cteAmount": {
//              "getCoin": "711471355771"
//          }
//      },
//      {
//          "cteId": "87710df809570678b258750853ed4a9c630e655ec21c8d9861fcb5289f7a7646",
//          "cteTimeIssued": 1521712751,
//          "cteAmount": {
//              "getCoin": "1009999820800"
//          }
//      },
//      {
//          "cteId": "8c9548b0cecebc6a9bde29389f91e3235c05b2326d7a607159047eff80685752",
//          "cteTimeIssued": 1521712671,
//          "cteAmount": {
//              "getCoin": "9387633447629"
//          }
//      },
//      {
//          "cteId": "80ca57c6c54f26adabecc50d734754aeadae30656a55be1c5cc943f8085cd1cf",
//          "cteTimeIssued": 1521712651,
//          "cteAmount": {
//              "getCoin": "453516000000"
//          }
//      },
//      {
//          "cteId": "904153331e97f3c3fac1be745d3449717129801558da589a2e07a32796788cd5",
//          "cteTimeIssued": 1521712631,
//          "cteAmount": {
//              "getCoin": "20954371782412"
//          }
//      },
//      {
//          "cteId": "5928b8f5a62b858ff4f1e4d01f719a5c6772f69fd50a44587d5bedd0301cf11d",
//          "cteTimeIssued": 1521712611,
//          "cteAmount": {
//              "getCoin": "3580198051486"
//          }
//      },
//      {
//          "cteId": "d8eb1fd5e24aab024f896df839a12ee6daae6a1cf1a6032b3761ddcc0c9c9aa1",
//          "cteTimeIssued": 1521712611,
//          "cteAmount": {
//              "getCoin": "12392433706"
//          }
//      },
//      {
//          "cteId": "7f35c60b26b55572e20db78db0647bbd57d0d110c6f4c61bb37659018ef15dbf",
//          "cteTimeIssued": 1521712571,
//          "cteAmount": {
//              "getCoin": "49999829018"
//          }
//      },
//      {
//          "cteId": "2892df3501e44045e5ed636715d3c7e7d2072a06817c92b8757f07d1f0d1d3ad",
//          "cteTimeIssued": 1521712571,
//          "cteAmount": {
//              "getCoin": "40001329018"
//          }
//      },
//      {
//          "cteId": "708f15f57bc664204b1bd3b39a1e81dc637285bdeffb99431b57dba8035777a6",
//          "cteTimeIssued": 1521712531,
//          "cteAmount": {
//              "getCoin": "20965382723091"
//          }
//      },
//      {
//          "cteId": "9e1b062bda65f040abc8ce847212b4630737f5fdf1224cd3ccbfc534b3e10d34",
//          "cteTimeIssued": 1521712491,
//          "cteAmount": {
//              "getCoin": "300425101317"
//          }
//      },
//      {
//          "cteId": "6614f2a3b05ee1bc5590bf43e71e4d4d6260f36c5adbf0be64c42765d301fdc2",
//          "cteTimeIssued": 1521712451,
//          "cteAmount": {
//              "getCoin": "20982414665279"
//          }
//      },
//      {
//          "cteId": "c5844c5c4cf3f2f1cdba40d98255c964e00c4758bc1ba19b0d903fb358f9f8c1",
//          "cteTimeIssued": 1521712391,
//          "cteAmount": {
//              "getCoin": "20993353225337"
//          }
//      },
//      {
//          "cteId": "6b4424ce1abe090e09c8e1d16915aaa8465f31d2c6e5d7eba74ca86786623121",
//          "cteTimeIssued": 1521712371,
//          "cteAmount": {
//              "getCoin": "40001525955"
//          }
//      },
//      {
//          "cteId": "72671334d8c92de6f49e6588b6e7398f83b311d2aa80e9b1871daccfc6f322d3",
//          "cteTimeIssued": 1521712351,
//          "cteAmount": {
//              "getCoin": "20998641278156"
//          }
//      }
//  ]
//}
	@Data
	public class TransactionEntry{
		String cteId;
		Long cteTimeIssued;
		CCoin cteAmount;	
	}
	
	List<TransactionEntry> Right = new ArrayList<TransactionEntry>();

}
