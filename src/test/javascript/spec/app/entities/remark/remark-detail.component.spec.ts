import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { MyappTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { RemarkDetailComponent } from '../../../../../../main/webapp/app/entities/remark/remark-detail.component';
import { RemarkService } from '../../../../../../main/webapp/app/entities/remark/remark.service';
import { Remark } from '../../../../../../main/webapp/app/entities/remark/remark.model';

describe('Component Tests', () => {

    describe('Remark Management Detail Component', () => {
        let comp: RemarkDetailComponent;
        let fixture: ComponentFixture<RemarkDetailComponent>;
        let service: RemarkService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MyappTestModule],
                declarations: [RemarkDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    RemarkService,
                    EventManager
                ]
            }).overrideComponent(RemarkDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RemarkDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RemarkService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Remark(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.remark).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
