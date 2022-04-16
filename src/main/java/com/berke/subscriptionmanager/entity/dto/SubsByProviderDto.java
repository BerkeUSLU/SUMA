package com.berke.subscriptionmanager.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
@NamedNativeQuery(
        name = "get_subscriptions_by_provider",
        query =
                "SELECT provider_name as providerName, sum(monthly_fee) as sumFee " +
                    "FROM all_subscriptions sub " +
                    "left join service_provider sp on " +
                    "sub.service_id = sp.service_id " +
                    "left join provider p on " +
                    "sp.provider_id = p.provider_id " +
                    "WHERE user_id = (:userId) " +
                    "group by p.provider_name;",
        resultSetMapping = "subscriptions_by_provider_dto"
)
@SqlResultSetMapping(
        name = "subscriptions_by_provider_dto",
        classes = @ConstructorResult(
                targetClass = SubsByProviderDto.class,
                columns = {
                        @ColumnResult(name = "providerName", type = String.class),
                        @ColumnResult(name = "sumFee", type = Double.class)
                }
        )
)*/
/*@Data*/
public interface SubsByProviderDto {
    String getProviderName();
    Double getSumFee();

}
