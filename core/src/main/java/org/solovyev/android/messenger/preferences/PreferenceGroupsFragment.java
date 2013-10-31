package org.solovyev.android.messenger.preferences;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.solovyev.android.fragments.DetachableFragment;
import org.solovyev.android.messenger.BaseListFragment;
import org.solovyev.android.messenger.BaseListItemAdapter;
import org.solovyev.android.messenger.api.MessengerAsyncTask;
import org.solovyev.android.messenger.core.R;
import org.solovyev.android.view.ListViewAwareOnRefreshListener;

/**
 * User: serso
 * Date: 3/17/13
 * Time: 5:56 PM
 */
public final class PreferenceGroupsFragment extends BaseListFragment<PreferenceGroup, PreferenceGroupListItem> implements DetachableFragment {

	public static final String FRAGMENT_TAG = "preference-groups";

	public PreferenceGroupsFragment() {
		super(FRAGMENT_TAG, false, true);
	}

	@Nullable
	@Override
	protected ListViewAwareOnRefreshListener getTopPullRefreshListener() {
		return null;
	}

	@Nullable
	@Override
	protected ListViewAwareOnRefreshListener getBottomPullRefreshListener() {
		return null;
	}

	@Override
	protected boolean canReuseFragment(@Nonnull Fragment fragment, @Nonnull PreferenceGroupListItem selectedItem) {
		boolean canReuse = false;
		if(fragment instanceof PreferenceListFragment) {
			canReuse = selectedItem.getPreferenceGroup().getPreferencesResId() == ((PreferenceListFragment) fragment).getPreferencesResId();
		}
		return canReuse;
	}

	@Nonnull
	@Override
	protected BaseListItemAdapter<PreferenceGroupListItem> createAdapter() {
		final List<PreferenceGroupListItem> preferences = new ArrayList<PreferenceGroupListItem>();

		preferences.add(new PreferenceGroupListItem(new PreferenceGroup("preferences-appearance", R.string.mpp_preferences_appearance, R.xml.mpp_preferences_appearance, R.drawable.mpp_settings_appearance_states)));
		preferences.add(new PreferenceGroupListItem(new PreferenceGroup("preferences-others", R.string.mpp_preferences_other, R.xml.mpp_preferences_others, R.drawable.mpp_settings_other_states)));

		return new PreferencesAdapter(this.getActivity(), preferences);
	}

	@Nullable
	@Override
	protected MessengerAsyncTask<Void, Void, List<PreferenceGroup>> createAsyncLoader(@Nonnull BaseListItemAdapter<PreferenceGroupListItem> adapter, @Nonnull Runnable onPostExecute) {
		return null;
	}
}
