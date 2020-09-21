package com.gaia.results.demo.model;

import java.util.List;

public class Gaia_result {

        private String solutionid;
        private String designation;
        private int sourceid;
        private int randomindex;

        public Gaia_result(String solutionid, String designation, int sourceid, int randomindex) {
            this.solutionid = solutionid;
            this.designation = designation;
            this.sourceid = sourceid;
            this.randomindex = randomindex;
        }

        public String getSolutionid() {
            return solutionid;
        }

    public void setSolutionid(String solutionid) {
        this.solutionid = solutionid;
    }
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}
