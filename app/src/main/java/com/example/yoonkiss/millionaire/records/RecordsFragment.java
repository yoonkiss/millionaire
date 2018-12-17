/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.yoonkiss.millionaire.records;

import android.support.v4.app.Fragment;

import com.example.yoonkiss.millionaire.data.Record;

import java.util.List;


public class RecordsFragment extends Fragment implements RecordsContract.View {

    @Override
    public void setLoadingIndicator(boolean active) {
        
    }

    @Override
    public void showRecords(List<Record> records) {

    }

    @Override
    public void showNoRecords() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setPresenter(RecordsContract.Presenter presenter) {

    }
}
