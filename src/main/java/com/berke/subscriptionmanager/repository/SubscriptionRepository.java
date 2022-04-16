package com.berke.subscriptionmanager.repository;

import com.berke.subscriptionmanager.entity.Subscription;
import com.berke.subscriptionmanager.entity.dto.SubsByProviderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription as s WHERE s.id=?1 AND s.serviceProvider=?2")
    List<Subscription> getActivePeriod(int userId, int providerId);

    @Query("SELECT subs FROM Subscription AS subs JOIN subs.subscriptionUser AS sub_user WHERE sub_user.id=?1")
    List<Subscription> getSubscriptionsByUserId(long userId);

    /*@Query("SELECT p.providerName as providerName, sum(sp.monthlyFee) as sumFee FROM Subscription AS sub " +
            "left join ServiceProvider AS sp on sub.serviceProvider.id=sp.id " +
            "left join Provider AS p on p.id=sp.provider.id " +
            "where sub.subscriptionUser.id=?1 " +
            " GROUP BY p.providerName")*/
    @Query(value = "SELECT p.provider_name as providerName, SUM(sp.monthly_fee) as sumFee " +
            "FROM " +
            "all_subscriptions  AS subs " +
            "LEFT JOIN service_provider AS sp " +
            "ON sp.service_id=subs.service_id " +
            "JOIN provider AS p " +
            "ON p.provider_id=sp.provider_id " +
            "WHERE subs.is_active = 1 AND subs.user_id= :user_id " +
            "GROUP BY p.provider_name; ", nativeQuery = true)
    //@Query(name = "get_subscriptions_by_provider", nativeQuery = true)
    List<SubsByProviderDto> getSubscriptionsMonthlyByServiceProvider(@Param("user_id") long userId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE all_subscriptions SET is_active = 0 WHERE subs_end_date < now()", nativeQuery = true)
    void deactivateExpiredSubscriptions();

    //Expenses by month
    @Query(value = "SELECT SUM(sp.monthly_fee) as expenses " +
            "FROM " +
            "all_subscriptions  AS subs " +
            "LEFT JOIN service_provider AS sp " +
            "ON sp.service_id=subs.service_id " +
            "WHERE subs.is_active = 1 AND subs.user_id= :userId"
            , nativeQuery = true)
    Optional<Double> getMonthlyExpenses(@Param("userId") long userId);

    @Query("SELECT subs FROM Subscription AS subs WHERE subs.subsEndDate BETWEEN ?1 AND ?2" )
    List<Subscription> findAllBySubscriptionEndTimeBetween(LocalDate endDateStart, LocalDate endDateEnd);

    @Query("SELECT subs FROM Subscription AS subs WHERE subs.subsEndDate=?1" )
    List<Subscription> findAllBySubsEndDate(LocalDate date);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM all_subscriptions WHERE user_id= :userId ; ", nativeQuery = true)
    void deleteSubscriptionsByUser(@Param("userId") long userId);

}