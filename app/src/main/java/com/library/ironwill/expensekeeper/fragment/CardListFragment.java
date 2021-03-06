package com.library.ironwill.expensekeeper.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.library.ironwill.expensekeeper.R;
import com.library.ironwill.expensekeeper.activity.MainActivity;
import com.library.ironwill.expensekeeper.adapter.ItemRvCategoryAdapter;
import com.library.ironwill.expensekeeper.adapter.RvCategoryAdapter;
import com.library.ironwill.expensekeeper.helper.TransitionHelper;
import com.library.ironwill.expensekeeper.model.ItemCategory;
import com.library.ironwill.expensekeeper.util.Navigator;
import com.library.ironwill.expensekeeper.view.ExplosionView.ExplosionField;
import com.library.ironwill.expensekeeper.view.IronRecyclerView.CustomLinearLayoutManager;
import com.library.ironwill.expensekeeper.view.IronRecyclerView.HidingScrollListener;
import com.library.ironwill.expensekeeper.view.IronRecyclerView.RecyclerViewClickListener;
import com.library.ironwill.expensekeeper.view.IronRecyclerView.SimpleDividerItemDecoration;
import com.library.ironwill.expensekeeper.view.RandomTextView.RandomTextView;

import java.util.ArrayList;

public class CardListFragment extends TransitionHelper.BaseFragment implements RvCategoryAdapter.OnRemoveItemListener { // implements OnStartDragListener

    //    private IronRecyclerView mRecyclerView;
    private RecyclerView mRecyclerView;
    //    private RvCategoryAdapter mAdapter;
    private ItemRvCategoryAdapter mAdapter;
    private CustomLinearLayoutManager cLinearLayoutManager;
    private ItemCategory mCategory;
    private static ArrayList<ItemCategory> mList;
    private RandomTextView rtvIncome, rtvExpense;
//    private ItemTouchHelper mItemTouchHelper;

    public FloatingActionButton addFABtn, doneFABtn;
    private BottomSheetBehavior mBehavior;
    private View mBottomSheet;
    private EditText titleText, contentText;

    private int lastVisibleItem;
    private int totalItemCount;
    private LinearLayout statisticContainer;


    public CardListFragment() {
    }

    protected ArrayList<ItemCategory> getList() {
        mList = new ArrayList<>();
        mCategory = new ItemCategory(R.drawable.salary, "Salary", "$ " + 235, 1);
        mList.add(0, mCategory);
        mCategory = new ItemCategory(R.drawable.food, "Food", "$ " + 40, 0);
        mList.add(1, mCategory);
        mCategory = new ItemCategory(R.drawable.movie, "Movie", "$ " + 30, 0);
        mList.add(2, mCategory);
        mCategory = new ItemCategory(R.drawable.gym, "Gym", "$ " + 10, 0);
        mList.add(3, mCategory);
        mCategory = new ItemCategory(R.drawable.gas, "Gas", "$ " + 50, 0);
        mList.add(4, mCategory);
        mCategory = new ItemCategory(R.drawable.rent, "Rent", "$ " + 30, 0);
        mList.add(5, mCategory);
        return mList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_list);
        statisticContainer = (LinearLayout) rootView.findViewById(R.id.statistic_container);
        addFABtn = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        doneFABtn = (FloatingActionButton) rootView.findViewById(R.id.fab_done);
        mBottomSheet = rootView.findViewById(R.id.id_rl_bottomSheet);
        titleText = (EditText) rootView.findViewById(R.id.id_et_new_name);
        contentText = (EditText) rootView.findViewById(R.id.id_et_new_num);
        mRecyclerView.setFocusable(false);
        statisticContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.launchDetail(MainActivity.of(getActivity()), view, statisticContainer);
            }
        });


        ExplosionField explosionField = new ExplosionField(getActivity());
        explosionField.addListener(rootView.findViewById(R.id.cardView_main));

        rtvIncome = (RandomTextView) rootView.findViewById(R.id.rtv_income);
        rtvExpense = (RandomTextView) rootView.findViewById(R.id.rtv_expense);
        rtvIncome.setText("235");
        rtvIncome.setPianyilian(RandomTextView.FIRSTF_FIRST);
        rtvIncome.start();
        rtvExpense.setText("160");
        rtvExpense.setPianyilian(RandomTextView.FIRSTF_FIRST);
        rtvExpense.start();
        initRecyclerList();
        initFBtnAction();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rtvExpense.destroy();
        rtvIncome.destroy();
    }

    private void initRecyclerList() {
        mRecyclerView.setHasFixedSize(true);
        cLinearLayoutManager = new CustomLinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(cLinearLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        mAdapter = new ItemRvCategoryAdapter();
        mAdapter.updateList(getList(), getActivity());
        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(), new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Navigator.launchDetail(MainActivity.of(getActivity()), view, mRecyclerView);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                MainActivity.of(getActivity()).animateHomeIcon(MaterialMenuDrawable.IconState.X);
            }
        }));
        mRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onShow() {
                addFABtn.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onHide() {
                addFABtn.animate().translationY(400)
                        .setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

        /*mAdapter.setOnRVItemLongClickListener(new RvCategoryAdapter.onRVItemLongClickListener() {
            @Override
            public void onRVItemLongClick(int position) {
                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(mRecyclerView);
            }
        });*/
        mRecyclerView.setAdapter(mAdapter);
        DefaultItemAnimator mIronItemAnimator = new DefaultItemAnimator();
        mRecyclerView.setItemAnimator(mIronItemAnimator);
    }


    @Override
    public void onRemoveListener() {
        lastVisibleItem = cLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        totalItemCount = cLinearLayoutManager.getItemCount();
        if (lastVisibleItem == totalItemCount - 1) {
            addFABtn.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }

    private void openMenu(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, 65, 135);
        animator.setDuration(500);
        animator.start();
    }

    private void closeMenu(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 135, 65, 0);
        animator.setDuration(500);
        animator.start();
    }

    private void initFBtnAction() {
        /**
         * BottomSheet Behavior programming
         */
        if (mBottomSheet != null) {
            mBehavior = BottomSheetBehavior.from(mBottomSheet);
        }

        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (mBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING) {
                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    doneFABtn.setVisibility(View.VISIBLE);
                    ObjectAnimator animatorX = ObjectAnimator.ofFloat(doneFABtn, "scaleX", 1f, 1.14f, 1f);
                    ObjectAnimator animatorY = ObjectAnimator.ofFloat(doneFABtn, "scaleY", 1f, 1.14f, 1f);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.play(animatorX).with(animatorY);
                    animSet.setDuration(500);
                    animSet.start();
                    cLinearLayoutManager.setScrollEnabled(false);
                    mRecyclerView.setLayoutManager(cLinearLayoutManager);
                } else if (mBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    doneFABtn.setVisibility(View.GONE);
                    ObjectAnimator animatorX = ObjectAnimator.ofFloat(doneFABtn, "scaleX", 1f, 0f);
                    ObjectAnimator animatorY = ObjectAnimator.ofFloat(doneFABtn, "scaleY", 1f, 0f);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.playTogether(animatorX, animatorY);
                    animSet.setDuration(500);
                    animSet.start();
                    cLinearLayoutManager.setScrollEnabled(true);
                    mRecyclerView.setLayoutManager(cLinearLayoutManager);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        addFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    openMenu(view);
                } else {
                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    closeMenu(view);
                }
            }
        });


        doneFABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!titleText.getText().toString().equals("") && !contentText.getText().toString().equals("")) {

                    mList.add(new ItemCategory(
                            R.mipmap.ic_launcher,
                            titleText.getText().toString(),
                            "$ " + contentText.getText().toString(),
                            0
                    ));
                    mAdapter = new ItemRvCategoryAdapter();
                    mAdapter.updateList(mList, getActivity());
                    mRecyclerView.setAdapter(mAdapter);
                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    titleText.setText("");
                    closeMenu(addFABtn);
                    contentText.setText("");
                } else {
                    Toast.makeText(getActivity(), "Title and Content are null",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onBeforeBack() {
//        MainActivity activity = MainActivity.of(getActivity());
       /* if (!activity.animateHomeIcon(MaterialMenuDrawable.IconState.BURGER)) {
            activity.mDrawer.openDrawer(GravityCompat.START);
        }*/
        return super.onBeforeBack();
    }




/*    @Override
    public void onStartDrag(RvCategoryAdapter.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }*/
}

