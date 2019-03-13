/**
 *  Author :  hmg25
 *  Description :
 */
package com.book.guide;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import java.util.Arrays;
import java.util.Comparator;


public class ImagePageFactory {
	int mImgNum;
	int mImgIndex;
	Bitmap mPage;
	
	private List<String> fileList = new ArrayList<String>();

	private boolean m_isfirstPage,m_islastPage;

	public ImagePageFactory() {
	}

    static Comparator<File> mComparator = new Comparator<File>() {
        @Override
        public int compare(File lhs, File rhs) {
            return rhs.getName().compareTo(lhs.getName());
        }
    };

	public void openbook(String strFilePath) throws IOException {
		File dir = new File(strFilePath);
		final FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File candidate) {
				return candidate.getName().endsWith(".png");
			}
			
		};
		final File[] files = dir.listFiles(filter);
        Arrays.sort(files, mComparator);
		mImgNum = files.length;
		for(int i=files.length-1;i>=0;i--) {
			fileList.add(files[i].getPath());
		}
	}

	protected void pageDown() {
		mImgIndex++;
	}

	protected void pageUp() {
		mImgIndex--;
	}
	
	protected Bitmap getPage() {
		BitmapFactory.Options options = new BitmapFactory.Options();  
		Bitmap b = BitmapFactory.decodeFile(fileList.get(mImgIndex), options); 
		
		return b;
	}

	protected void prePage() throws IOException {
		if (mImgIndex <= 0) {
			mImgIndex = 0;
			m_isfirstPage=true;
			return;
		}else m_isfirstPage=false;
		pageUp();
		mPage = getPage();
	}

	public void nextPage() throws IOException {
		if (mImgIndex >= mImgNum-1) {
			m_islastPage=true;
			return;
		}else m_islastPage=false;
		pageDown();
		mPage = getPage();
	}

	public void draw(Canvas c) throws IOException{
		if (mPage == null)
			mPage = getPage();
		c.drawBitmap(mPage, 0, 0, null);
	}
	
	public boolean isfirstPage() {
		return m_isfirstPage;
	}
	public boolean islastPage() {
		return m_islastPage;
	}
}
