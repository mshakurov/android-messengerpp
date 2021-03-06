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

package org.solovyev.android.messenger.users;

import org.solovyev.android.messenger.App;
import org.solovyev.android.messenger.chats.Chat;
import org.solovyev.android.messenger.chats.ChatEvent;
import org.solovyev.android.messenger.entities.Entity;
import org.solovyev.android.messenger.entities.EntityAwareRemovedUpdater;
import org.solovyev.common.collections.multimap.*;
import org.solovyev.common.listeners.AbstractJEventListener;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.List;

import static org.solovyev.common.collections.multimap.ThreadSafeMultimap.newThreadSafeMultimap;

@ThreadSafe
class UserChats {

	// key: user entity, value: list of user chats
	@Nonnull
	private final ThreadSafeMultimap<Entity, Chat> chats = newThreadSafeMultimap();

	@Nonnull
	public List<Chat> getChats(@Nonnull Entity user) {
		return chats.get(user);
	}

	void init() {
		App.getChatService().addListener(new ChatEventListener());
	}

	public void update(@Nonnull Entity user, @Nonnull List<Chat> chats) {
		if (!chats.isEmpty()) {
			this.chats.update(user, new WholeListUpdater<Chat>(chats));
		} else {
			this.chats.remove(user);
		}
	}

	void onEvent(@Nonnull UserEvent event) {
		final User eventUser = event.getUser();

		switch (event.getType()) {
			case chat_added:
				final Chat chat = event.getDataAsChat();
				this.chats.update(eventUser.getEntity(), new ObjectAddedUpdater<Chat>(chat));
				break;
			case chats_added:
				final List<Chat> chats = event.getDataAsChats();
				this.chats.update(eventUser.getEntity(), new ObjectsAddedUpdater<Chat>(chats));
				break;
			case chat_removed:
				final String removedChatId = event.getDataAsChatId();
				this.chats.update(eventUser.getEntity(), new EntityAwareRemovedUpdater<Chat>(removedChatId));
				break;
		}
	}

	private class ChatEventListener extends AbstractJEventListener<ChatEvent> {

		protected ChatEventListener() {
			super(ChatEvent.class);
		}

		@Override
		public void onEvent(@Nonnull ChatEvent event) {
			switch (event.getType()) {
				case changed:
					chats.update(new ObjectsChangedMapUpdater<Entity, Chat>(event.getChat()));
					break;

			}
		}
	}
}
