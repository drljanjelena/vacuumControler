import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVacuumComponent } from './add-vacuum.component';

describe('AddVacuumComponent', () => {
  let component: AddVacuumComponent;
  let fixture: ComponentFixture<AddVacuumComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddVacuumComponent]
    });
    fixture = TestBed.createComponent(AddVacuumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
