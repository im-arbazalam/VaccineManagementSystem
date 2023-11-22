package com.example.VaccineManagementSystem.Service;

import com.example.VaccineManagementSystem.Enum.AppointmentStatus;
import com.example.VaccineManagementSystem.Exceptions.*;
import com.example.VaccineManagementSystem.Models.Appointment;
import com.example.VaccineManagementSystem.Models.Doctor;
import com.example.VaccineManagementSystem.Models.User;
import com.example.VaccineManagementSystem.Repository.AppointmentRepository;
import com.example.VaccineManagementSystem.Repository.DoctorRepository;
import com.example.VaccineManagementSystem.Repository.UserRepository;
import com.example.VaccineManagementSystem.RequestDtos.AppointmentReqDto;
import com.example.VaccineManagementSystem.RequestDtos.CancelAppointmentRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;
    public String bookAppointment(AppointmentReqDto appointmentReqDto) throws DoctorNotFound,UserNotFound{

        Optional<Doctor> doctorOptional = doctorRepository.findById(appointmentReqDto.getDocId());

        if(!doctorOptional.isPresent()){
            throw new DoctorNotFound("DoctorId not found");
        }
        Optional<User> userOptional = userRepository.findById(appointmentReqDto.getUserId());
        if(!userOptional.isPresent()){
            throw new UserNotFound("UserId not found");
        }

        Doctor doctor = doctorOptional.get();
        User user = userOptional.get();
        Appointment appointment = new Appointment();

        //Creating the object and setting of its attributes
        appointment.setAppointmentDate(appointmentReqDto.getAppointmentDate());
        appointment.setAppointmentTime(appointmentReqDto.getAppointmentTime());
        appointment.setAppointmentStatus(appointmentReqDto.getAppointmentStatus());

        //setting up the foreign key attributes
        appointment.setDoctor(doctor);
        appointment.setUser(user);

        //saving it before so that it can get the primary key of the appointment table
        appointment = appointmentRepository.save(appointment);

        doctor.getAppointmentList().add(appointment);
        user.getAppointmentList().add(appointment);

        doctorRepository.save(doctor);
        userRepository.save(user);

        return "Appointment made successfully";
    }


    public String deleteAppointment(CancelAppointmentRequestDto cancelAppointmentRequestDto)throws AppointmentCanNotDelete,AppointmentNotFound,UserNotFound,UserDoNotHaveAppointmentId {
        Integer userId= cancelAppointmentRequestDto.getUserId();
        Integer appointmentId= cancelAppointmentRequestDto.getAppointmentId();

        Optional<Appointment> appointmentOptional=appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isEmpty()){
            throw new AppointmentNotFound("Appointment Not Found");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()){
            throw new UserNotFound("User not Found");
        }

        User user=userOpt.get();

        Appointment appointment =appointmentOptional.get();

        Doctor doctor=appointment.getDoctor();

        if (!appointment.getUser().equals(user)){
            throw new UserDoNotHaveAppointmentId("User is not having a appointment");
        }

        if(appointment.getAppointmentStatus().equals(AppointmentStatus.COMPLETED)){
            throw new AppointmentCanNotDelete();
        }

        user.getAppointmentList().remove(appointment);
        doctor.getAppointmentList().remove(appointment);
        appointmentRepository.delete(appointment);
        return "Your appointmentId: "+appointmentId+" has been Deleted successfully";

    }


}