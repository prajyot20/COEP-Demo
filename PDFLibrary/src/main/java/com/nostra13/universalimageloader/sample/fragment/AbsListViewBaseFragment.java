/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
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
 *******************************************************************************/
package com.nostra13.universalimageloader.sample.fragment;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.artifex.mupdfdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.sample.Constants;
import com.nostra13.universalimageloader.sample.activity.AudioActivity2;
import com.nostra13.universalimageloader.sample.activity.DrawerActivity;
import com.nostra13.universalimageloader.sample.activity.SimpleImageActivity;
import com.nostra13.universalimageloader.sample.activity.VideoActivity;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class AbsListViewBaseFragment extends BaseFragment {

    protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
    protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

    protected AbsListView listView;

    protected boolean pauseOnScroll = false;
    protected boolean pauseOnFling = true;

    @Override
    public void onResume() {
        super.onResume();
        applyScrollListener();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem pauseOnScrollItem = menu.findItem(R.id.item_pause_on_scroll);
        pauseOnScrollItem.setVisible(true);
        pauseOnScrollItem.setChecked(pauseOnScroll);

        MenuItem pauseOnFlingItem = menu.findItem(R.id.item_pause_on_fling);
        pauseOnFlingItem.setVisible(true);
        pauseOnFlingItem.setChecked(pauseOnFling);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.item_pause_on_scroll:
                pauseOnScroll = !pauseOnScroll;
                item.setChecked(pauseOnScroll);
                applyScrollListener();
                return true;
            case R.id.item_pause_on_fling:
                pauseOnFling = !pauseOnFling;
                item.setChecked(pauseOnFling);
                applyScrollListener();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/return false;
    }

    protected void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), DrawerActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImagePagerFragment.INDEX);
        intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        startActivity(intent);
    }

    protected void startPDFPagerActivity(String filename) {
        Intent intent = new Intent(getActivity(), PdfViewFragment.class);
        intent.putExtra("filename", filename);
        //intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        startActivity(intent);
    }

    protected void startGIFImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), SimpleImageActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageGIFPagerFragment.INDEX);
        intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        startActivity(intent);
    }


    protected void startVideoPagerActivity(int position) {
        Intent intent = new Intent(getActivity(), SimpleImageActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, VideoPagerFragment.INDEX);
        intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        startActivity(intent);
    }

    protected void startVideoPagerActivityNew(String urlbase) {
        Intent intent = new Intent(getActivity(), VideoActivity.class);
        //intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageMusicPagerFragment.INDEX);
        //intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        intent.putExtra("url111", urlbase);
        startActivity(intent);

    }

    protected void startAugioPagerActivity(int position, String urlbase) {
        Intent intent = new Intent(getActivity(), SimpleImageActivity.class);
        intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageMusicPagerFragment.INDEX);
        intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        intent.putExtra("url", urlbase);
        startActivity(intent);
    }

    protected void startAugioPagerActivityNew(String urlbase) {
        Intent intent = new Intent(getActivity(), AudioActivity2.class);
        //intent.putExtra(Constants.Extra.FRAGMENT_INDEX, ImageMusicPagerFragment.INDEX);
        //intent.putExtra(Constants.Extra.IMAGE_POSITION, position);
        intent.putExtra("url111", urlbase);
        startActivity(intent);
    }

    private void applyScrollListener() {
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling));
    }
}
