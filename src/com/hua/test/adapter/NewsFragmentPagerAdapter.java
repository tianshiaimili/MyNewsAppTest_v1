
package com.hua.test.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.hua.test.utils.LogUtils2;

/**
 * HomePage news adapter
 * 
 * @author zero
 *
 */
public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();;
    private final FragmentManager fm;

    public NewsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    public NewsFragmentPagerAdapter(FragmentManager fm,
            ArrayList<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    public void appendList(ArrayList<Fragment> fragment) {
        fragments.clear();
        if (!fragments.containsAll(fragment) && fragment.size() > 0) {
            fragments.addAll(fragment);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        if (this.fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        return obj;
    }

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		LogUtils2.e("NewsFragmentPagerAdapter.position = "+position);
		LogUtils2.i("NewsFragmentPagerAdapter.object = "+object);
	}

	@Override
	public void startUpdate(ViewGroup viewgroup) {
		super.startUpdate(viewgroup);
//		LogUtils2.e("NewsFragmentPagerAdapter.startUpdate = ");
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
//		LogUtils2.e("NewsFragmentPagerAdapter.finishUpdate = ");
	}

	@Override
	public Parcelable saveState() {
		LogUtils2.e("NewsFragmentPagerAdapter.saveState = ");
		return super.saveState();
	}

	@Override
	public void restoreState(Parcelable parcelable, ClassLoader classloader) {
		LogUtils2.e("NewsFragmentPagerAdapter.restoreState = ");
		super.restoreState(parcelable, classloader);
	}
    
    

}
