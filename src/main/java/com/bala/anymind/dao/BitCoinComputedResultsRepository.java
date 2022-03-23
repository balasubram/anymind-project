package com.bala.anymind.dao;

import com.bala.anymind.model.BitCoinComputedResultsDBModel;
import com.bala.anymind.model.BitCoinRequestDBModel;
import org.springframework.data.jpa.repository.JpaRepository;
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
import java.util.Objects;
import java.util.Set;

@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class BitCoinComputedResultsRepository extends AbstractDAO<BitCoinComputedResultsDBModel> {

    @Override
    protected Class<BitCoinComputedResultsDBModel> getEntityClass() {
        return BitCoinComputedResultsDBModel.class;
    }


    public List<BitCoinComputedResultsDBModel> retrieveResults(Long beginTimestamp, Long endTimestamp) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BitCoinComputedResultsDBModel> criteriaQuery = criteriaBuilder.createQuery(BitCoinComputedResultsDBModel.class);
        Root<BitCoinComputedResultsDBModel> bitCoinComputedResultsDBModelRoot = criteriaQuery.from(BitCoinComputedResultsDBModel.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(bitCoinComputedResultsDBModelRoot.get("computedTimeStamp"), beginTimestamp));
        predicates.add(criteriaBuilder.lessThan(bitCoinComputedResultsDBModelRoot.get("computedTimeStamp"), endTimestamp));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<BitCoinComputedResultsDBModel> retrieveComputedResults(Set<Long> computedTimestamps) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BitCoinComputedResultsDBModel> criteriaQuery = criteriaBuilder.createQuery(BitCoinComputedResultsDBModel.class);
        Root<BitCoinComputedResultsDBModel> bitCoinComputedResultsDBModelRoot = criteriaQuery.from(BitCoinComputedResultsDBModel.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(bitCoinComputedResultsDBModelRoot.get("computedTimeStamp").in(computedTimestamps));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public void createOrUpdateRecords(List<BitCoinComputedResultsDBModel> bitCoinComputedResultsDBModels) {
        for (BitCoinComputedResultsDBModel bitCoinComputedResultsDBModel : bitCoinComputedResultsDBModels) {
            if(Objects.isNull(bitCoinComputedResultsDBModel.getBitCoinRequestsId())) {
                super.create(bitCoinComputedResultsDBModel);
            }  else {
                super.update(bitCoinComputedResultsDBModel);
            }
        }
    }

    public List<BitCoinComputedResultsDBModel> retrieveComputedResultsGreaterThan(Long timestamp) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BitCoinComputedResultsDBModel> criteriaQuery = criteriaBuilder.createQuery(BitCoinComputedResultsDBModel.class);
        Root<BitCoinComputedResultsDBModel> bitCoinComputedResultsDBModelRoot = criteriaQuery.from(BitCoinComputedResultsDBModel.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(bitCoinComputedResultsDBModelRoot.get("computedTimeStamp"), timestamp));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(criteriaBuilder.asc(bitCoinComputedResultsDBModelRoot.get("computedTimeStamp")));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<BitCoinComputedResultsDBModel> retrieveAllResults() {
        return super.findAll();
    }

}