-- Drop tables if they exist to ensure a clean setup (optional, but good for dev)
DROP TABLE IF EXISTS interview_evaluation;
DROP TABLE IF EXISTS interview_appointment;

-- Create interview_appointment table
CREATE TABLE interview_appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_name VARCHAR(255) NOT NULL,
    interview_time TIMESTAMP,
    interviewer VARCHAR(255),
    status VARCHAR(50),
    notes TEXT
);

-- Create interview_evaluation table
CREATE TABLE interview_evaluation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT,
    interviewer_comments TEXT,
    rating INT,
    result VARCHAR(50),
    evaluation_time TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES interview_appointment(id) ON DELETE CASCADE
);
