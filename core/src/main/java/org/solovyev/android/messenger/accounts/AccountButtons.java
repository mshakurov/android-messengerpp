package org.solovyev.android.messenger.accounts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import javax.annotation.Nonnull;

import org.solovyev.android.messenger.core.R;
import org.solovyev.android.messenger.sync.SyncAllAsyncTask;

import static org.solovyev.android.messenger.App.getSyncService;
import static org.solovyev.android.messenger.accounts.AccountUiEventType.edit_account;

public class AccountButtons extends BaseAccountButtons<Account<?>, AccountFragment>{

	public AccountButtons(@Nonnull AccountFragment fragment) {
		super(fragment);
	}

	@Override
	public void onViewCreated(View root, Bundle savedInstanceState) {
		super.onViewCreated(root, savedInstanceState);

		final AccountFragment fragment = getFragment();
		final Account<?> account = fragment.getAccount();

		final Button editButton = (Button) root.findViewById(R.id.mpp_account_edit_button);
		editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editAccount();
			}
		});

		final Button syncButton = (Button) root.findViewById(R.id.mpp_account_sync_button);
		syncButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SyncAllAsyncTask.newForAccount(getActivity(), getSyncService(), account).executeInParallel((Void) null);
			}
		});

		final Button changeStateButton = (Button) root.findViewById(R.id.mpp_account_state_button);
		changeStateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeState();
			}
		});
		if (!account.getRealm().isEnabled()) {
			changeStateButton.setEnabled(false);
		}
	}

	void editAccount() {
		getFragment().getEventManager().fire(edit_account.newEvent(getAccount()));
	}

	void changeState() {
		final AccountFragment fragment = getFragment();
		fragment.getTaskListeners().run(AccountChangeStateCallable.TASK_NAME, new AccountChangeStateCallable(getAccount()), AccountChangeStateListener.newInstance(getActivity()), getActivity(), R.string.mpp_saving_account_title, R.string.mpp_saving_account_message);
	}

	@Override
	protected boolean isBackButtonVisible() {
		return !getFragment().getMultiPaneManager().isDualPane(getActivity());
	}

	@Override
	protected void onSaveButtonPressed() {
	}

	@Override
	protected void onBackButtonPressed() {
		getActivity().getSupportFragmentManager().popBackStack();
	}

	void updateAccountViews(@Nonnull Account<?> account, @Nonnull View root) {
		final Button syncButton = (Button) root.findViewById(R.id.mpp_account_sync_button);
		final Button changeStateButton = (Button) root.findViewById(R.id.mpp_account_state_button);
		if (account.isEnabled()) {
			changeStateButton.setText(R.string.mpp_disable);
			syncButton.setVisibility(View.VISIBLE);
		} else {
			changeStateButton.setText(R.string.mpp_enable);
			syncButton.setVisibility(View.GONE);
		}
	}
}
