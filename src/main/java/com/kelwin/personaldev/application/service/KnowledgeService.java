package com.kelwin.personaldev.application.service;

import com.kelwin.personaldev.domain.model.Knowledge;
import com.kelwin.personaldev.domain.model.User;
import com.kelwin.personaldev.domain.repository.KnowledgeRepository;
import com.kelwin.personaldev.domain.repository.UserRepository;
import com.kelwin.personaldev.presentation.dto.knowledge.KnowledgeCreateRequest;
import com.kelwin.personaldev.presentation.dto.knowledge.KnowledgeResponse;
import com.kelwin.personaldev.presentation.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KnowledgeService {

    private final KnowledgeRepository knowledgeRepository;
    private final UserRepository userRepository;

    public KnowledgeResponse create(KnowledgeCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Knowledge knowledge = Knowledge.builder()
                .user(user)
                .title(request.title())
                .description(request.description())
                .createdAt(LocalDateTime.now())
                .build();

        Knowledge savedKnowledge = knowledgeRepository.save(knowledge);

        return toResponse(savedKnowledge);
    }

    public List<KnowledgeResponse> findAll() {
        return knowledgeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public KnowledgeResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public KnowledgeResponse update(
            UUID id,
            KnowledgeCreateRequest request
    ) {
        Knowledge knowledge = findEntityById(id);

        knowledge.update(
            request.title(),
            request.description()
        );

        Knowledge updatedKnowledge = knowledgeRepository.save(knowledge);

        return toResponse(updatedKnowledge);
    }

    public void delete(UUID id) {
        Knowledge knowledge = findEntityById(id);
        knowledgeRepository.delete(knowledge);
    }

    private Knowledge findEntityById(UUID id) {
        return knowledgeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Knowledge not found"));
    }

    private KnowledgeResponse toResponse(Knowledge knowledge) {
        return new KnowledgeResponse(
                knowledge.getId(),
                knowledge.getUser().getId(),
                knowledge.getTitle(),
                knowledge.getDescription(),
                knowledge.getCreatedAt()
        );
    }
}