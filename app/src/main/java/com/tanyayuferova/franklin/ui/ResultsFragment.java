package com.tanyayuferova.franklin.ui;

import android.animation.Animator;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyayuferova.franklin.FranklinApplication;
import com.tanyayuferova.franklin.database.VirtuesContract;
import com.tanyayuferova.franklin.databinding.FragmentResultsBinding;
import com.tanyayuferova.franklin.entity.Result;
import com.tanyayuferova.franklin.entity.Virtue;
import com.tanyayuferova.franklin.entity.VirtueResult;
import com.tanyayuferova.franklin.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.terrakok.cicerone.Router;

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
    private Date firstDate = DateUtils.getFirstDayOfWeek();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
    private int LOADER_ID = 12;

    public static ResultsFragment newInstance(Object data) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultsBinding.inflate(inflater, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
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
                firstDate = DateUtils.addDaysToDate(firstDate, -7);
                animateWeekTitle(true);
            }
        });
        binding.nectWeekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstDate = DateUtils.addDaysToDate(firstDate, 7);
                animateWeekTitle(false);
            }
        });
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        return binding.getRoot();
    }

    private void updateWeekTitle() {
        binding.weekTitleText.setText(dateFormat.format(firstDate) + " - " + dateFormat.format(DateUtils.addDaysToDate(firstDate, 6)));
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
                                " between '" + dateFormat.format(firstDate) + "' and '" + dateFormat.format(DateUtils.addDaysToDate(firstDate, 6))
                                + "') as 'current_week'",
                        " (select count(*) from " + VirtuesContract.PointEntry.TABLE_NAME +
                                " where " + VirtuesContract.VirtueEntry.TABLE_NAME + "." + VirtuesContract.VirtueEntry._ID +
                                " = " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_VIRTUE_ID +
                                " and " + VirtuesContract.PointEntry.TABLE_NAME + "." + VirtuesContract.PointEntry.COLUMN_DATE +
                                " between '" + dateFormat.format(DateUtils.addDaysToDate(firstDate, -7)) + "' and '" + dateFormat.format(DateUtils.addDaysToDate(firstDate, -1))
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

        List<VirtueResult> results = new ArrayList<>();
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            int current = data.getInt(data.getColumnIndex("current_week"));
            int previous = data.getInt(data.getColumnIndex("prev_week"));
            int result = current - previous;
            results.add(new VirtueResult(
                    Virtue.fromId(getContext(), data.getInt(data.getColumnIndex(VirtuesContract.VirtueEntry._ID))),
                    current,
                    result > 0 ? Result.NEGATIVE : result < 0 ? Result.POSITIVE : Result.NEUTRAL,
                    result > 0 ? Result.NEGATIVE : result < 0 ? Result.POSITIVE : Result.NEUTRAL
            ));
        }
        adapter.setData(results);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setData(null);
    }
}
