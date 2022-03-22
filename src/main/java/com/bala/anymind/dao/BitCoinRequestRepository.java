package com.bala.anymind.dao;

import com.bala.anymind.model.BitCoinComputedResultsDBModel;
import com.bala.anymind.model.BitCoinRequestDBModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class BitCoinRequestRepository extends AbstractDAO<BitCoinRequestDBModel> {

    @Override
    protected Class<BitCoinRequestDBModel> getEntityClass() {
        return BitCoinRequestDBModel.class;
    }


    public Long createNewRecord(BitCoinRequestDBModel bitCoinRequestDBModel) {
        bitCoinRequestDBModel = super. create(bitCoinRequestDBModel);
        return bitCoinRequestDBModel.getBitCoinRequestsId();
    }


    public List<BitCoinRequestDBModel> retrieveNonProcessedBitCoinRequests(long currentTimeStamp) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BitCoinRequestDBModel> criteriaQuery = criteriaBuilder.createQuery(BitCoinRequestDBModel.class);
        Root<BitCoinRequestDBModel> bitCoinRequestDBModelRoot = criteriaQuery.from(BitCoinRequestDBModel.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.lessThan(bitCoinRequestDBModelRoot.get("timeStamp"), currentTimeStamp));
        predicates.add(criteriaBuilder.equal(bitCoinRequestDBModelRoot.get("processed"), false));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public void updateRecords(List<BitCoinRequestDBModel> bitCoinRequestDBModels) {
        bitCoinRequestDBModels.forEach(b -> super.update(b));
    }

}