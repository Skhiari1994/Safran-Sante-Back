package com.tn.arabsoft.CaisseRetraite.DTO;

public class ResponseProcedure {

        private String message;
        private int totalLines;
        private int insertedLines;

        // getters & setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getTotalLines() {
            return totalLines;
        }

        public void setTotalLines(int totalLines) {
            this.totalLines = totalLines;
        }

        public int getInsertedLines() {
            return insertedLines;
        }

        public void setInsertedLines(int insertedLines) {
            this.insertedLines = insertedLines;
        }


}
