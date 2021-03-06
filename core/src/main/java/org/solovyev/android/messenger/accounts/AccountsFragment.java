/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.solovyev.android.messenger.accounts;

import android.content.Context;
import android.os.Bundle;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import org.solovyev.android.menu.ActivityMenu;
import org.solovyev.android.menu.IdentifiableMenuItem;
import org.solovyev.android.menu.ListActivityMenu;
import org.solovyev.android.messenger.App;
import org.solovyev.android.messenger.BaseListItemAdapter;
import org.solovyev.android.messenger.core.R;
import org.solovyev.android.sherlock.menu.SherlockMenuHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static org.solovyev.android.messenger.UiEventType.show_realms;
import static org.solovyev.android.messenger.accounts.AccountUiEventType.show_account;

public class AccountsFragment extends BaseAccountsFragment {

	@Nonnull
	public static final String FRAGMENT_TAG = "accounts";

	private ActivityMenu<Menu, MenuItem> menu;

	public AccountsFragment() {
		super("Accounts", R.string.mpp_accounts, false, true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Nonnull
	@Override
	protected BaseListItemAdapter<AccountListItem> createAdapter() {
		final List<AccountListItem> listItems = new ArrayList<AccountListItem>();
		for (Account account : getAccountService().getAccounts()) {
			if (account.getState() != AccountState.removed) {
				listItems.add(new AccountListItem(account, show_account));
			}
		}
		return new AccountsAdapter(getActivity(), listItems, true, show_account);
	}

    /*
	**********************************************************************
    *
    *                           MENU
    *
    **********************************************************************
    */

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (this.menu == null) {
			this.menu = ListActivityMenu.fromResource(R.menu.mpp_menu_accounts, AccountsMenu.class, SherlockMenuHelper.getInstance());
		}

		this.menu.onCreateOptionsMenu(this.getActivity(), menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		this.menu.onPrepareOptionsMenu(this.getActivity(), menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return this.menu.onOptionsItemSelected(this.getActivity(), item) || super.onOptionsItemSelected(item);
	}

	private static enum AccountsMenu implements IdentifiableMenuItem<MenuItem> {
		add_account(R.id.mpp_menu_add_account) {
			@Override
			public void onClick(@Nonnull MenuItem data, @Nonnull Context context) {
				App.getEventManager(context).fire(show_realms.newEvent());
			}
		};

		private final int menuItemId;

		AccountsMenu(int menuItemId) {
			this.menuItemId = menuItemId;
		}

		@Nonnull
		@Override
		public Integer getItemId() {
			return this.menuItemId;
		}
	}
}
