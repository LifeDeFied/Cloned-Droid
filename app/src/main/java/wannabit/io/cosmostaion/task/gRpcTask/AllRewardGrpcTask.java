package wannabit.io.cosmostaion.task.gRpcTask;


import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_ALL_REWARDS;
import static wannabit.io.cosmostaion.network.ChannelBuilder.TIME_OUT;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import cosmos.distribution.v1beta1.Distribution;
import cosmos.distribution.v1beta1.QueryGrpc;
import cosmos.distribution.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.base.BaseChain;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

public class AllRewardGrpcTask extends CommonTask {
    private BaseChain mChain;
    private String mAddress;
    private ArrayList<Distribution.DelegationDelegatorReward> mResultData = new ArrayList<>();
    private QueryGrpc.QueryBlockingStub mStub;

    public AllRewardGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain, String address) {
        super(app, listener);
        this.mChain = chain;
        this.mAddress = address;
        this.mResult.taskType = TASK_GRPC_FETCH_ALL_REWARDS;
        this.mStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(mChain)).withDeadlineAfter(TIME_OUT, TimeUnit.SECONDS);;
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            QueryOuterClass.QueryDelegationTotalRewardsRequest request = QueryOuterClass.QueryDelegationTotalRewardsRequest.newBuilder().setDelegatorAddress(mAddress).build();
            QueryOuterClass.QueryDelegationTotalRewardsResponse response = mStub.delegationTotalRewards(request);
            mResultData.addAll(response.getRewardsList());
            this.mResult.isSuccess = true;
            this.mResult.resultData = mResultData;
//            WLog.w("AllReward " + mResultData.size());

        } catch (Exception e) { WLog.e( "AllRewardGrpcTask "+ e.getMessage()); }
        return mResult;
    }
}
