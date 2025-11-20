# Milestone 3 - Architecture Analysis

**Team**: Bilciurescu Elena-Alina, Solomon Miruna-Maria, Toma Daria-Maria  
**Group**: 1241EA CTI-E


For Milestone 3 we had to analyze different software architectures and figure out which one makes sense for StreamFlix. We compared three approaches:

1. **Monolithic** - what we have now
2. **Microservices** - what we'll probably move to 
3. **Event-Driven** - cool but maybe overkill

## Files

- `Milestone_3_-_SDT.pdf` - the full analysis document with diagrams and everything
- Architecture diagrams (in the PDF):
  - Component diagrams for each architecture
  - Deployment diagrams showing how stuff would actually run

## The Architectures We Looked At

### Monolithic (Current)
Everything in one Spring Boot app with H2 database. Simple to run and test.

**Good**: Fast development, easy deployment, all our patterns work together  
**Bad**: Can't scale parts separately, one crash = everything down

### Microservices (Next Step)
Break it into separate services:
- User Service (auth, profiles)
- Content Service (movies/series) - Factory pattern
- Video Service (watch events) - Observer pattern  
- Recommendation Service (algorithms) - Strategy pattern
- Analytics Service
- Notification Service

**Good**: Scale independently, fault isolation, team can work in parallel  
**Bad**: More complex to deploy and debug

### Event-Driven
Everything talks through Kafka events instead of direct calls.

**Good**: Super loose coupling, can replay events, audit trail  
**Bad**: Eventual consistency issues, harder to debug, steep learning curve

## How to Run

Nothing to run for this milestone - it's just analysis and documentation. But for reference:
```bash
# Our current monolith (from Milestone 2)
mvn spring-boot:run
```

## Design Patterns Mapping

We figured out how our existing patterns map to microservices:

- **Factory Method** → Content Service (creates movies/series)
- **Strategy** → Recommendation Service (switches algorithms)
- **Observer** → Video Service (publishes events to message queue)
- **Singleton** → Each service has its own singletons (DB pool, cache, etc)

## Resources We Used

- Refactoring Guru (for pattern theory)
- Netflix tech blogs (they're basically doing what we want to do)
- Course slides on microservices patterns
- Those PDFs the prof gave us about integration patterns

## Next Steps

Milestone 4: Actually implement the microservices architecture with at least 3 services talking to each other via REST.

If you have questions about any of the architecture decisions, please refer to the PDF. 
