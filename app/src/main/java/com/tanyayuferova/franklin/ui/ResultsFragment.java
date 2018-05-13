package com.tanyayuferova.franklin.ui;

import android.animation.Animator;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyayuferova.franklin.FranklinApplication;
import com.tanyayuferova.franklin.R;
import com.tanyayuferova.franklin.database.VirtuesContract;
import com.tanyayuferova.franklin.databinding.FragmentResultsBinding;
import com.tanyayuferova.franklin.entity.Result;
import com.tanyayuferova.franklin.entity.Virtue;
import com.tanyayuferova.franklin.entity.VirtueResult;
import com.tanyayuferova.franklin.utils.DateUtils;
import com.tanyayuferova.franklin.utils.VirtueOfWeekUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ru.terrakok.cicerone.Router;

import static android.support.v7.widget.RecyclerView.VERTICAL;
import static com.tanyayuferova.franklin.database.VirtuesContract.CONTENT_VIRTUES_URI;

/**
 * Created by Tanya Yuferova on 4/25/2018.
 */

public class ResultsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    final public static String SCREEN_KEY = "RESULTS_FRAGMENT_SCREEN_KEY";
    private Router router = FranklinApplication.INSTANCE.getRouter();
    private FragmentResultsBinding binding;
    private VirtueResultsAdapter adapter;
    private Date currentDate;
    private DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
    public static final String STATE_CURRENT_DATE = "state.current_date";
    public static final String ARG_CURRENT_DATE = "arg.current_date";
    private int LOADER_ID = 12;

    public static ResultsFragment newInstance(Object data) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CURRENT_DATE, ((Date) data).getTime());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultsBinding.inflate(inflater, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);

        if (savedInstanceState != null) {
            currentDate = new Date(savedInstanceState.getLong(STATE_CURRENT_DATE));
        } else {
            currentDate = new Date(getArguments().getLong(ARG_CURRENT_DATE));
        }

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                router.exit();
            }
        });
        initRecyclerView();
        updateWeekTitle();
        binding.previousWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = DateUtils.addDaysToDate(currentDate, -7);
                updateData();
                updateWeekTitle();
                enableNextButton();
            }
        });
        binding.nectWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = DateUtils.addDaysToDate(currentDate, 7);
                updateData();
                updateWeekTitle();
                enableNextButton();
            }
        });
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        enableNextButton();
        updateData();
        return binding.getRoot();
    }

    private void enableNextButton() {
        binding.nectWeekBtn.setEnabled(Calendar.getInstance().getTime().getTime() - currentDate.getTime() > TimeUnit.DAYS.toMillis(7));
    }

    private void updateWeekTitle() {
        binding.weekTitleText.setText(dateFormat.format(currentDate) + " - " + dateFormat.format(DateUtils.addDaysToDate(currentDate, 6)));
    }

    private void animateWeekTitle(boolean slideRight) {
        int k = slideRight ? 1 : -1;
        binding.weekTitleText
                .animate()
                .translationX(k * binding.weekTitleText.getWidth())
                .setListener(
                        new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.weekTitleText.setTranslationX(0);
                                updateData();
                                updateWeekTitle();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }
                ).start();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.otherResults.setLayoutManager(layoutManager);
        binding.otherResults.setHasFixedSize(true);
        adapter = new VirtueResultsAdapter();
        adapter.setHasStableIds(true);
        binding.otherResults.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation()) {

            private final int[] ATTRS = new int[]{android.R.attr.listDivider};
            private Drawable mDivider;
            private final Rect mBounds = new Rect();

            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                final TypedArray a = parent.getContext().obtainStyledAttributes(ATTRS);
                mDivider = a.getDrawable(0);
                canvas.save();
                final int left;
                final int right;
                //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
                if (parent.getClipToPadding()) {
                    left = parent.getPaddingLeft();
                    right = parent.getWidth() - parent.getPaddingRight();
                    canvas.clipRect(left, parent.getPaddingTop(), right,
                            parent.getHeight() - parent.getPaddingBottom());
                } else {
                    left = 0;
                    right = parent.getWidth();
                }

                RecyclerView.ViewHolder child = parent.findViewHolderForAdapterPosition(1);
                if (child != null) {
                    parent.getDecoratedBoundsWithMargins(child.itemView, mBounds);
                    final int bottom = mBounds.bottom + Math.round(child.itemView.getTranslationY());
                    final int top = bottom - mDivider.getIntrinsicHeight();
                    mDivider.setBounds(left, top, right, bottom);
                    mDivider.draw(canvas);
                }
                canvas.restore();
            }
        };
        binding.otherResults.addItemDecoration(dividerItemDecoration);
    }

    private void updateData() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String sortOrder = VirtuesContract.VirtueEntry._ID + " ASC";
        return new CursorLoader(getContext(),
                CONTENT_VIRTUES_URI,
                new String[]{
                        VirtuesContract.VirtueEntry._ID,
                        " (select count(*) from " + VirtuesContract.PointEntry.TABLE_NAME +
                                " where " + VirtuesContract.VirtueEntry.TABLE_NAME + "." + VirtuesContract.VirtueEntry._ID +
                                " = " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_VIRTUE_ID +
                                " and " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_DATE +
                                " between '" + dateFormat.format(currentDate) + "' and '" + dateFormat.format(DateUtils.addDaysToDate(currentDate, 6))
                                + "') as 'current_week'",
                        " (select count(*) from " + VirtuesContract.PointEntry.TABLE_NAME +
                                " where " + VirtuesContract.VirtueEntry.TABLE_NAME + "." + VirtuesContract.VirtueEntry._ID +
                                " = " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_VIRTUE_ID +
                                " and " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_DATE +
                                " between '" + dateFormat.format(DateUtils.addDaysToDate(currentDate, -7)) + "' and '" + dateFormat.format(DateUtils.addDaysToDate(currentDate, -1))
                                + "') as 'prev_week'"
                },
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (getContext() == null)
            return; //fixme

        List<Object> results = new ArrayList<>();
        int indexCurrent = 0;
        int virtueOfTheWeekId = VirtueOfWeekUtils.getVirtueIdOfWeek(getContext(), currentDate);
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            int id = data.getInt(data.getColumnIndex(VirtuesContract.VirtueEntry._ID));
            int current = data.getInt(data.getColumnIndex("current_week"));
            int previous = data.getInt(data.getColumnIndex("prev_week"));
            int result = current - previous;
            results.add(new VirtueResult(
                    Virtue.fromId(getContext(), id),
                    current,
                    result > 0 ? Result.NEGATIVE : result < 0 ? Result.POSITIVE : Result.NEUTRAL,
                    result > 0 ? Result.NEGATIVE : result < 0 ? Result.POSITIVE : Result.NEUTRAL
            ));
            if (id == virtueOfTheWeekId) {
                indexCurrent = i;
            }
        }
        results.add(0, results.get(indexCurrent));
        results.remove(indexCurrent + 1);
        results.add(0, getString(R.string.virtue_of_the_week));
        results.add(2, getString(R.string.other_virtues));
        adapter.setData(results);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setData(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (outState == null)
            outState = new Bundle();
        outState.putLong(STATE_CURRENT_DATE, currentDate.getTime());
        super.onSaveInstanceState(outState);
    }
}
