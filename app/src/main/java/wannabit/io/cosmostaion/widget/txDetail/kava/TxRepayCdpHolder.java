package wannabit.io.cosmostaion.widget.txDetail.kava;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cosmos.tx.v1beta1.ServiceOuterClass;
import kava.cdp.v1beta1.Tx;
import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.base.BaseData;
import wannabit.io.cosmostaion.base.chains.ChainConfig;
import wannabit.io.cosmostaion.utils.WDp;
import wannabit.io.cosmostaion.widget.txDetail.TxHolder;

public class TxRepayCdpHolder extends TxHolder {
    TextView  itemMsgTitle;
    TextView  itemSender, itemCdpDenom, itemPaymentAmount, itemPaymentDenom;

    public TxRepayCdpHolder(@NonNull View itemView) {
        super(itemView);
        itemMsgTitle = itemView.findViewById(R.id.tx_msg_text);
        itemSender = itemView.findViewById(R.id.tx_cdp_sender);
        itemCdpDenom = itemView.findViewById(R.id.tx_cdp_denom);
        itemPaymentAmount = itemView.findViewById(R.id.tx_payment_amount);
        itemPaymentDenom = itemView.findViewById(R.id.tx_payment_symbol);
    }

    public void onBindMsg(Context c, BaseData baseData, ChainConfig chainConfig, ServiceOuterClass.GetTxResponse response, int position, String address) {
        try {
            Tx.MsgRepayDebt msg = Tx.MsgRepayDebt.parseFrom(response.getTx().getBody().getMessages(position).getValue());
            itemSender.setText(msg.getSender());
            itemCdpDenom.setText(msg.getCollateralType());
            WDp.setDpCoin(c, baseData, chainConfig, msg.getPayment().getDenom(), msg.getPayment().getAmount(), itemPaymentDenom, itemPaymentAmount);

        } catch (Exception e) { }
    }
}
