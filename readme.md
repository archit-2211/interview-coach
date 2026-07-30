# Interview Management System Back End System developed using Java Springboot 

## Overview

Interview Management System is a backend application that enables candidates to connect with verified interviewers, schedule mock interviews, attend interviews through meeting links, and receive structured feedback.

The platform supports three types of users:

* Candidate
* Interviewer
* Admin

The system ensures that only verified candidates and interviewers can participate in the interview process.

---

# Functional Requirements

## Candidate Features

* Register and login
* Create and update profile
* Upload resume
* View profile verification status
* Search interviewers by skill
* View interviewer details
* View available interview slots
* Request an interview
* View interview history
* View interview status
* View interview feedback

---

## Interviewer Features

* Register and login
* Create and update profile
* Add and update skills
* View profile verification status
* Create availability slots
* Update availability slots
* Delete availability slots
* View interview requests
* Accept interview requests
* Reject interview requests
* Add meeting link for accepted interviews
* View upcoming interviews
* Submit interview feedback

---

## Admin Features

* Login
* View all candidates
* View all interviewers
* View pending candidate verifications
* View pending interviewer verifications
* Approve candidate profiles
* Reject candidate profiles
* Approve interviewer profiles
* Reject interviewer profiles
* View all interviews
* Cancel interviews

---

# Business Rules

## Registration

* A user can register as either Candidate or Interviewer.
* Email addresses must be unique.
* Only authenticated users can access protected APIs.

---

## Candidate Verification

* Newly created candidate profiles are unverified.
* Unverified candidates cannot request interviews.
* Only approved candidates can request interviews.

---

## Interviewer Verification

* Newly created interviewer profiles are unverified.
* Candidates can search interviewers based on skills.
* Candidates can only view approved interviewers.

---

## Interview Requests

* A candidate can request an interview using an available slot.
* A slot can only be booked once.
* Candidate and interviewer must both be approved.
* Every interview request starts in a REQUESTED state.

---

## Interview Acceptance

* Interviewers can accept interview requests.
* Interviewers can reject interview requests.
* Accepted requests become scheduled interviews.

---

## Meeting Link

* Only accepted interviews can have a meeting link.
* The interviewer is responsible for providing the meeting link.

---

## Feedback

* Feedback can only be submitted after an interview.
* Only the assigned interviewer can submit feedback.
* Candidates can view feedback for their own interviews.

---

## Security

* Candidates cannot access interviewer-only functionality.
* Interviewers cannot access admin functionality.
* Admins can access verification and management functionality.

---

# User Journey

Candidate Registration
→ Candidate Profile Creation
→ Admin Verification
→ Search Interviewers
→ Select Available Slot
→ Request Interview
→ Interviewer Accepts Request
→ Meeting Link Added
→ Interview Conducted
→ Feedback Submitted
→ Candidate Views Feedback

---

# Project Scope (V1)

Included:

* Authentication and Authorization
* Candidate Management
* Interviewer Management
* Admin Verification Workflow
* Interview Scheduling
* Availability Management
* Interview Feedback Management

# Future Scope of Development (V2)

* AI Agents
* Live Coding Platform
* Video Conferencing
* Real-Time Notifications
* Analytics Dashboard
* Calendar Integrations
* Automated Interview Evaluation
* WebSocket-based Features


# Domain Model

## User

Represents an authenticated user of the platform.

### Attributes

| Field                 | Type                  | Description                      |
| -----------           | ----------            | -------------------------------- |
| userId                | UUID                  | Unique identifier                |
| email                 | String                | User email address (unique)      |
| password              | String                | Encrypted password               |
| phoneNumber           | String                | Contact number                   |
| userRole              | UserRole              | Candidate, Interviewer, or Admin |
| createdAt             | DateTime              | TimeStamp of creation time.      |
| userStatus            | UserStatus            | Verification status              |
| name                  | String                | Name of the user                 |
| authenticationSource  | AuthenticationSource  | To provide AuthenticatonSource   |             |

---

## Profile

Represents user profile information shared across candidates and interviewers.

### Attributes

| Field           | Type                 | Description             |
| --------------- | -------------------- | ----------------------- |
| profileId       | UUID                 | Unique identifier       |
| user            | User                 | Associated user         |
| resumes         | List<Resume>         | Uploaded resumes        |
| skills          | List<Skill>          | User skills             |
| workExperiences | List<WorkExperience> | Professional experience |
| rating          | Double               | Profile rating          |

---

## Resume

Stores resume metadata.

### Attributes

| Field      | Type          | Description        |
| ---------- | ------------- | -----------------  |
| resumeId   | UUID          | Unique identifier  |
| fileName   | String        | Resume file name   | 
| fileUrl    | String        | Storage location   |
| uploadedAt | LocalDateTime | Upload timestamp   |
| profile    | Profile       | profile associated |

---

## Skill

Represents a technical or professional skill.

### Attributes

| Field     | Type   | Description       |
| --------- | ------ | ----------------- |
| skillId   | UUID   | Unique identifier |
| skillName | String | Skill name        |

### Examples

* Java
* Spring Boot
* React
* Microservices
* System Design

---

## WorkExperience

Represents a professional work experience record.

### Attributes

| Field            | Type      | Description           |
| ---------------- | --------- | --------------------- |
| workExperienceId | UUID      | Unique identifier     |
| profile          | Profile   | Associated profile    |
| companyName      | String    | Company name          |
| startDate        | LocalDate | Employment start date |
| endDate          | LocalDate | Employment end date   |

---

## SlotTiming

Represents a time window available for interviews.

### Attributes

| Field     | Type      | Description    |
| --------- | --------- | -------------- |
| date      | LocalDate | Interview date |
| startTime | LocalTime | Start time     |
| endTime   | LocalTime | End time       |

---

## Slot

Represents an interview availability slot created by an interviewer.

### Attributes

| Field       | Type       | Description                 |
| ----------- | ---------- | --------------------------- |
| slotId      | UUID       | Unique identifier           |
| interviewer | User       | Interviewer owning the slot |
| slotTiming  | SlotTiming | Slot schedule               |

---

## InterviewRequest

Represents a candidate's request for an interview.

### Attributes

| Field                  | Type                   | Description                        |
| ---------------------- | ---------------------- | ---------------------------------- |
| requestId              | UUID                   | Unique identifier                  |
| candidateUser          | User                   | Candidate requesting the interview |
| interviewerUser        | User                   | Interviewer receiving the request  |
| slot                   | Slot                   | Selected slot                      |
| topics                 | List<Skill>            | Requested interview topics         |
| interviewRequestStatus | InterviewRequestStatus | Request status                     |

---

## Interview

Represents a scheduled interview.

### Attributes

| Field            | Type             | Description                |
| ---------------- | ---------------- | -------------------------- |
| interviewId      | UUID             | Unique identifier          |
| interviewRequest | InterviewRequest | Accepted interview request |
| interviewStatus  | InterviewStatus  | Interview lifecycle status |
| meetingLink      | String           | Meeting URL                |
| feedback         | Feedback         | Interview feedback         |

---

## Feedback

Represents interview feedback submitted after interview completion.

### Attributes

| Field               | Type    | Description                      |
| ------------------- | ------- | -------------------------------- |
| feedbackId          | UUID    | Unique identifier                |
| userFeedback        | String  | Feedback provided by candidate   |
| interviewerFeedback | String  | Feedback provided by interviewer |
| userRating          | Integer | Candidate rating                 |
| interviewerRating   | Integer | Interviewer rating               |

---

# Enums

## UserRole

```text
CANDIDATE
INTERVIEWER
ADMIN
```

---

## UserStatus

```text
NEW
VERIFIED
LOCKED
```

---

## InterviewRequestStatus

```text
PENDING
ACCEPTED
REJECTED
```

---

## AuthenticationSource 
```text
GOOGLE
LOCAL
```



## InterviewStatus

```text
SCHEDULED
COMPLETED
CANCELLED
```



# Entity Relationships

## User ↔ Profile

```text
User (1) -------- (1) Profile
```

Each user has exactly one profile, and each profile belongs to exactly one user.

---

## Profile ↔ Resume

```text
Profile (1) -------- (N) Resume
```

A profile can contain multiple resumes.

---

## Profile ↔ WorkExperience

```text
Profile (1) -------- (N) WorkExperience
```

A profile can contain multiple work experience records.

---

## Profile ↔ Skill

```text
Profile (N) -------- (N) Skill
```

A profile can have multiple skills, and a skill can belong to multiple profiles.

---

## User (Interviewer) ↔ Slot

```text
User (1) -------- (N) Slot
```

An interviewer can create multiple availability slots.

Each slot belongs to exactly one interviewer.

---

## User (Candidate) ↔ InterviewRequest

```text
User (1) -------- (N) InterviewRequest
```

A candidate can create multiple interview requests.

Each interview request belongs to exactly one candidate.

---

## User (Interviewer) ↔ InterviewRequest

```text
User (1) -------- (N) InterviewRequest
```

An interviewer can receive multiple interview requests.

Each interview request targets exactly one interviewer.

---

## Slot ↔ InterviewRequest

```text
Slot (1) -------- (1) InterviewRequest
```

A slot can be associated with only one interview request.

Each interview request is created for exactly one slot.

---

## InterviewRequest ↔ Skill

```text
InterviewRequest (N) -------- (N) Skill
```

An interview request can contain multiple requested topics.

A skill can appear in multiple interview requests.

---

## InterviewRequest ↔ Interview

```text
InterviewRequest (1) -------- (1) Interview
```

An accepted interview request results in one interview.

Each interview is created from exactly one interview request.

---

## Interview ↔ Feedback

```text
Interview (1) -------- (1) Feedback
```

Each completed interview can have one feedback record.

Each feedback record belongs to one interview.



# API Endpoints

## Authentication APIs

| Method | Endpoint            | Description          |
| ------ | ------------------- | -------------------- |
| POST   | /auth/register      | Register a new user  |
| POST   | /auth/login         | Authenticate user    |
| POST   | /auth/logout        | Logout user          |
| POST   | /auth/refresh-token | Refresh access token |

---

## Profile APIs

| Method | Endpoint              | Description                   |
| ------ | --------------------- | ----------------------------- |
| GET    | /profiles/me          | Get current user's profile    |
| PUT    | /profiles/me          | Update current user's profile |
| GET    | /profiles/{profileId} | Get profile by ID             |

---

## Resume APIs

| Method | Endpoint                        | Description          |
| ------ | ------------------------------- | -------------------- |
| POST   | /profiles/me/resumes            | Upload resume        |
| GET    | /profiles/me/resumes            | Get uploaded resumes |
| DELETE | /profiles/me/resumes/{resumeId} | Delete resume        |

---

## Skill APIs

| Method | Endpoint          | Description    |
| ------ | ----------------- | -------------- |
| GET    | /skills           | Get all skills |
| GET    | /skills/search    | Search skills  |
| POST   | /skills           | Create skill   |
| PUT    | /skills/{skillId} | Update skill   |
| DELETE | /skills/{skillId} | Delete skill   |

---

## Slot APIs

| Method | Endpoint        | Description              |
| ------ | --------------- | ------------------------ |
| POST   | /slots          | Create availability slot |
| GET    | /slots/me       | Get current user's slots |
| PUT    | /slots/{slotId} | Update slot              |
| DELETE | /slots/{slotId} | Delete slot              |

---

## Interviewer APIs

| Method | Endpoint                     | Description                        |
| ------ | ---------------------------- | ---------------------------------- |
| GET    | /interviewers                | Get all interviewers               |
| GET    | /interviewers/{userId}       | Get interviewer details            |
| GET    | /interviewers/search         | Search interviewers by skill       |
| GET    | /interviewers/{userId}/slots | Get interviewer availability slots |

---

## Interview Request APIs

| Method | Endpoint                               | Description                           |
| ------ | -------------------------------------- | ------------------------------------- |
| POST   | /interview-requests                    | Create interview request              |
| GET    | /interview-requests/me                 | Get current user's interview requests |
| PUT    | /interview-requests/{requestId}/accept | Accept interview request              |
| PUT    | /interview-requests/{requestId}/reject | Reject interview request              |

---

## Interview APIs

| Method | Endpoint                               | Description                   |
| ------ | -------------------------------------- | ----------------------------- |
| GET    | /interviews/me                         | Get current user's interviews |
| GET    | /interviews/{interviewId}              | Get interview details         |
| PUT    | /interviews/{interviewId}/meeting-link | Add or update meeting link    |
| PUT    | /interviews/{interviewId}/complete     | Mark interview as completed   |
| PUT    | /interviews/{interviewId}/cancel       | Cancel interview              |

---

## Feedback APIs

| Method | Endpoint                           | Description            |
| ------ | ---------------------------------- | ---------------------- |
| POST   | /interviews/{interviewId}/feedback | Submit feedback        |
| GET    | /interviews/{interviewId}/feedback | Get interview feedback |

---

## Admin APIs

| Method | Endpoint                           | Description                       |
| ------ | ---------------------------------- | --------------------------------- |
| GET    | /admin/candidates                  | Get all candidates                |
| GET    | /admin/interviewers                | Get all interviewers              |
| GET    | /admin/pending-verifications       | Get pending verification requests |
| PUT    | /admin/profiles/{profileId}/verify | Verify profile                    |
| PUT    | /admin/profiles/{profileId}/lock   | Lock profile                      |
| GET    | /admin/interviews                  | Get all interviews                |
