/*
 * Eu InLoop Copyright(C) 2104
 * Modifications Copyright (C) 2015 Fred Grott(aka shareme GrottWorkShop)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under License.
 */
package com.github.shareme.gwsagedwhiskey.library.viewmodel;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * ViewModelProvider
 * Created by fgrott on 11/19/2015.
 */
@SuppressWarnings("unused")
public class ViewModelProvider {

    private final HashMap<String, AbstractViewModel<? extends IView>> mViewModelCache;

    public ViewModelProvider() {
        mViewModelCache = new HashMap<>();
    }

    boolean remove(@NonNull String key) {
        return mViewModelCache.remove(key) != null;
    }

    public static class ViewModelWrapper<T extends IView> {
        @NonNull
        public final AbstractViewModel<T> viewModel;
        public final boolean wasCreated;

        private ViewModelWrapper(@NonNull AbstractViewModel<T> mViewModel, boolean mWasCreated) {
            this.viewModel = mViewModel;
            this.wasCreated = mWasCreated;
        }
    }

    /**
     * Call this in {@link android.app.Activity#onStop()} if {@link android.app.Activity#isFinishing()}
     */
    public void removeAllViewModels() {
        mViewModelCache.clear();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public synchronized <T extends IView> ViewModelWrapper<T> getViewModel(@NonNull String key, @NonNull Class<? extends AbstractViewModel<T>> viewModelClass) {
        AbstractViewModel<T> instance = (AbstractViewModel<T>) mViewModelCache.get(key);
        if (instance != null) {
            return new ViewModelWrapper<>(instance, false);
        }

        try {
            instance = viewModelClass.newInstance();
            mViewModelCache.put(key, instance);
            return new ViewModelWrapper<>(instance, true);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
