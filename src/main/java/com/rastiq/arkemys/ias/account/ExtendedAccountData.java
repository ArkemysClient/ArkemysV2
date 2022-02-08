package com.rastiq.arkemys.ias.account;

import com.rastiq.arkemys.ias.tools.alt.AccountData;
/**
 * @author The_Fireplace
 * @deprecated Inconvenient. Insure (saved user passwords). Used only for conversion from old accounts to new accounts.
 */
@Deprecated
public class ExtendedAccountData extends AccountData {
	private static final long serialVersionUID = -909128662161235160L;
	public Boolean premium;
	public int[] lastused;
	public int useCount;
}
