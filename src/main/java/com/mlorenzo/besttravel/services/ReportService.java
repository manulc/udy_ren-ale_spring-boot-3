package com.mlorenzo.besttravel.services;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ReportService {
    void exportToExcel(HttpServletResponse response) throws IOException;
}
