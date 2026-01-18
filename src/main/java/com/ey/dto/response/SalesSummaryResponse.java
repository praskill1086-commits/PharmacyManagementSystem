package com.ey.dto.response;

import java.time.LocalDate;

public class SalesSummaryResponse {
		private LocalDate fromDate;
	    private LocalDate toDate;
	    private long numberOfSales;
	    private double totalRevenue;
		public LocalDate getFromDate() {
			return fromDate;
		}
		public void setFromDate(LocalDate fromDate) {
			this.fromDate = fromDate;
		}
		public LocalDate getToDate() {
			return toDate;
		}
		public void setToDate(LocalDate toDate) {
			this.toDate = toDate;
		}
		public long getNumberOfSales() {
			return numberOfSales;
		}
		public void setNumberOfSales(long numberOfSales) {
			this.numberOfSales = numberOfSales;
		}
		public double getTotalRevenue() {
			return totalRevenue;
		}
		public void setTotalRevenue(double totalRevenue) {
			this.totalRevenue = totalRevenue;
		}
	    
	    
	    

}
