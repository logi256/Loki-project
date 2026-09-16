# Smart Bike Pass System (Android)

A modern Android application built with Jetpack Compose, Kotlin, and Room Database for managing student campus bike passes, digital verification, and QR code access control.

## Overview

Migrated from the original Python/Flask application, this native Android app preserves all original workflows and data structures while delivering a clean, mobile-first Material 3 user experience:

- **Student Pass Application**: Complete form with validation for student information, vehicle details (registration number, vehicle type), and document attachment verification (RC Book, Driving License, Insurance Copy).
- **Application Status Tracking**: Real-time status lookup by Pass ID (e.g., `SBPS-D5C279BA`) with quick-select history, full review timeline, and remarks.
- **Approved Digital QR Pass**: Celebratory pass with campus branding, verified vehicle details, scannable QR code (`pass_id|vehicle_no|full_name`), validity badge, and native Android sharing.
- **Transport In-Charge Review**: Dedicated review dashboard to examine submitted documents, record verification notes, approve/forward to Principal, or reject with feedback.
- **Principal Approval Portal**: Executive sign-off interface for transport-verified applications with 1-click pass generation and QR issuance.
- **Administrator Dashboard**: System-wide statistics (Total, Pending, Transport OK, Approved, Rejected), searchable application database, and complete audit logging trail.

## Architecture

- **UI Framework**: Jetpack Compose with Material 3 theming
- **Local Persistence**: Room Database (Applications, Users, Audit Logs)
- **State Management**: Android Jetpack ViewModel with Kotlin Coroutines & StateFlow
- **Navigation**: Jetpack Navigation Compose
- **Platform**: Android SDK 36, Kotlin 2.2.10, AGP 9.1.1, Java 21

## Default Staff Credentials

- **Transport In-Charge**: `transport` / `transport123`
- **Principal**: `principal` / `principal123`
- **Administrator**: `admin` / `admin123`
