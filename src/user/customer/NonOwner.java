package user.customer;

import java.util.*;

public abstract class NonOwner extends Customer {
    private HashMap<ID, ApplicantDetail> applicants;

    public NonOwner(String email, String passwd) {
        super(email, passwd);
        applicants = new HashMap<ID, ApplicantDetail>();
    }

    public List<ApplicantDetail> getApplicants(List<ID> ids) {
        if (ids == null)
            return new ArrayList<ApplicantDetail>(applicants.values());
        ArrayList<ApplicantDetail> res = new ArrayList<ApplicantDetail>();
        for (ID id : ids)
            res.add(this.applicants.get(id));
        return res;
    }

    public boolean hasApplicant(ID id) {
        return applicants.containsKey(id);
    }

    public void addApplicant(ApplicantDetail d, boolean override)
                                throws ApplicantExistException {
        if (!override && applicants.containsKey(d.getId()))
            throw new ApplicantExistException();
        this.applicants.put(d.getId(), d);
    }

    public void addApplicant(ApplicantDetail d) throws ApplicantExistException {
        addApplicant(d, false);
    }

}