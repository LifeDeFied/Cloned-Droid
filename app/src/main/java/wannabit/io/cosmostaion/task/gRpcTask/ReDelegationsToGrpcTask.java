package wannabit.io.cosmostaion.task.gRpcTask;

import static wannabit.io.cosmostaion.base.BaseConstant.TASK_GRPC_FETCH_REDELEGATIONS_TO;

import cosmos.staking.v1beta1.QueryGrpc;
import cosmos.staking.v1beta1.QueryOuterClass;
import wannabit.io.cosmostaion.base.BaseApplication;
import wannabit.io.cosmostaion.base.BaseChain;
import wannabit.io.cosmostaion.network.ChannelBuilder;
import wannabit.io.cosmostaion.task.CommonTask;
import wannabit.io.cosmostaion.task.TaskListener;
import wannabit.io.cosmostaion.task.TaskResult;
import wannabit.io.cosmostaion.utils.WLog;

public class ReDelegationsToGrpcTask extends CommonTask {
    private BaseChain mChain;
    private String mAddress;
    private String mValToAddress;
    private QueryGrpc.QueryBlockingStub mStub;

    public ReDelegationsToGrpcTask(BaseApplication app, TaskListener listener, BaseChain chain, String address, String valToAddress) {
        super(app, listener);
        this.mChain = chain;
        this.mAddress = address;
        this.mValToAddress = valToAddress;
        this.mResult.taskType = TASK_GRPC_FETCH_REDELEGATIONS_TO;
        this.mStub = QueryGrpc.newBlockingStub(ChannelBuilder.getChain(mChain));
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            QueryOuterClass.QueryRedelegationsRequest request = QueryOuterClass.QueryRedelegationsRequest.newBuilder().setDelegatorAddr(mAddress).setSrcValidatorAddr(mValToAddress).build();
            QueryOuterClass.QueryRedelegationsResponse response = mStub.redelegations(request);

            this.mResult.isSuccess = true;
            this.mResult.resultData = response.getRedelegationResponsesList();
//            WLog.w("ReDelegationsToGrpcTask " + response.getRedelegationResponsesList().size());
//            WLog.w("ReDelegationsToGrpcTask " + response);

        } catch (Exception e) { WLog.e( "ReDelegationsToGrpcTask "+ e.getMessage()); }
        return mResult;
    }

}
